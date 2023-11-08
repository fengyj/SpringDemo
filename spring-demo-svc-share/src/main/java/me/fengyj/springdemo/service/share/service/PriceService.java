package me.fengyj.springdemo.service.share.service;

import me.fengyj.common.exceptions.ErrorSeverity;
import me.fengyj.common.exceptions.GeneralException;
import me.fengyj.common.utils.StringUtils;
import me.fengyj.springdemo.dao.PriceDao;
import me.fengyj.springdemo.dao.ShareClassOperationDao;
import me.fengyj.springdemo.dao.database.PriceDbDao;
import me.fengyj.springdemo.models.Price;
import me.fengyj.springdemo.models.ShareClassDailyKey;
import me.fengyj.springdemo.utils.exceptions.UserInvalidInputException;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

@Service
public class PriceService {

    private final YahooFinancialService yahooSvc;
    private final PriceDao priceDao;
    private final ShareClassOperationDao shareClassOperationDao;

    public PriceService(
            @Autowired PriceDbDao priceDao,
            @Autowired ShareClassOperationDao shareClassOperationDao,
            @Autowired YahooFinancialService yahooSvc) {
        this.priceDao = priceDao;
        this.shareClassOperationDao = shareClassOperationDao;
        this.yahooSvc = yahooSvc;
    }

    public List<Price> getHistory(String shareClassId) {

        if (StringUtils.isNullOrWhiteSpace(shareClassId))
            throw new UserInvalidInputException(ErrorSeverity.Error, null, "The shareClassId is not specified.", null);

        return priceDao.getList(shareClassId).values().stream().toList();
    }

    public List<Price> reloadAndUpdate(String shareClassId) {

        if (StringUtils.isNullOrWhiteSpace(shareClassId))
            throw new UserInvalidInputException(ErrorSeverity.Error, null, "The shareClassId is not specified.", null);

        var operationData = shareClassOperationDao.getByKey(shareClassId);
        if (operationData == null)
            throw new UserInvalidInputException(ErrorSeverity.Error, shareClassId, "The share cannot be found.", null);

        String rawData = null;
        try {
            rawData = yahooSvc.getHistoryPrices(operationData.getSymbol());
        } catch (Exception ex) {
            throw new GeneralException(
                    ErrorSeverity.Error,
                    String.format(
                            "Failed to load the price data of %s (symbol: %s) from Yahoo.",
                            shareClassId,
                            operationData.getSymbol()),
                    ex);
        }

        var priceList = parseResponseFromYahoo(shareClassId, rawData);

        var priceData = new TreeMap<ShareClassDailyKey, Price>();
        priceList.forEach(p -> priceData.put(p.getKey(), p));

        priceDao.save(shareClassId, priceData);

        return priceList;
    }

    public static List<Price> parseResponseFromYahoo(String shareClassId, String response) {

        var prices = new ArrayList<Price>();
        var format = CSVFormat.Builder.create()
                                      .setDelimiter(',')
                                      .setHeader("Date", "Open", "High", "Low", "Close", "Adj Close", "volume")
                                      .setNullString("null")
                                      .setRecordSeparator('\n')
                                      .setSkipHeaderRecord(true)
                                      .build();

        try (var parser = CSVParser.parse(response, format)) {
            parser.forEach(r -> {
                var date = r.get(0);
                var open = r.get(1);
                var high = r.get(2);
                var low = r.get(3);
                var close = r.get(4);
                var adj = r.get(5);
                var vol = r.get(6);
                if (close == null || vol == null || adj == null) return;

                var p = new Price();
                p.setKey(new ShareClassDailyKey());
                p.getKey().setShareClassId(shareClassId);
                p.getKey().setAsOfDate(LocalDate.parse(date));
                if (open != null) p.setOpenPrice(new BigDecimal(open));
                if (high != null) p.setHigh(new BigDecimal(high));
                if (low != null) p.setLow(new BigDecimal(low));
                p.setClosePrice(new BigDecimal(close));
                p.setAdjustedPrice(new BigDecimal(adj));
                p.setVolume(Long.parseLong(vol));

                prices.add(p);
            });
        } catch (IOException ex) {
            throw new GeneralException(ErrorSeverity.Error, "Cannot load price data from Yahoo.", ex);
        }
        return prices;
    }
}
