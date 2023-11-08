package me.fengyj.springdemo.service.share.service;

import me.fengyj.springdemo.dao.ShareClassOperationDao;
import me.fengyj.springdemo.dao.database.PriceDbDao;
import me.fengyj.springdemo.models.Price;
import me.fengyj.springdemo.models.ShareClassDailyKey;
import me.fengyj.springdemo.models.ShareClassOperation;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PriceServiceTest {

    @Mock
    private ShareClassOperationDao shareClassOperationDao;
    @Mock
    private PriceDbDao priceDbDao;
    @Mock
    private YahooFinancialService yahooSvc;

    @InjectMocks
    private PriceService priceSvc;

    @Test
    public void test_getHistory() {

        var shareClassId = "0P0000TEST";
        var expected = getMockData(shareClassId);
        var map = new TreeMap<ShareClassDailyKey, Price>();
        getMockData(shareClassId).forEach(p -> map.put(p.getKey(), p));

        when(priceDbDao.getList(shareClassId)).thenReturn(map);

        var actual = priceSvc.getHistory(shareClassId);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void test_reloadAndUpdate() {

        var shareClassId = "0P0000TEST";
        var shareClassOperation = new ShareClassOperation();
        shareClassOperation.setShareClassId(shareClassId);
        shareClassOperation.setSymbol("symbol");

        doNothing().when(priceDbDao).save(anyString(), any());
        when(shareClassOperationDao.getByKey(shareClassId)).thenReturn(shareClassOperation);
        when(yahooSvc.getHistoryPrices(shareClassOperation.getSymbol())).thenReturn(getYahooResponse());

        var expected = getMockData(shareClassId).toArray();
        var actual = priceSvc.reloadAndUpdate(shareClassId).toArray();

        Assertions.assertArrayEquals(expected, actual);
    }

    @Test
    public void test_parseResponseFromYahoo() {

        var shareClassId = "0P0000TEST";
        var response = getYahooResponse();

        var actual = PriceService.parseResponseFromYahoo(shareClassId, response);

        var expected = getMockData(shareClassId);

        Assertions.assertEquals(expected, actual);
    }

    private String getYahooResponse() {

        return """
                Date,Open,High,Low,Close,Adj Close,Volume
                2002-04-09,4.177316,4.324377,4.177316,4.236935,2.346507,1041885103
                2002-04-10,4.236935,4.252834,4.129621,4.213087,2.333299,170948728
                2002-05-06,null,null,null,null,null,null
                2002-05-07,null,null,null,null,null,null
                2002-05-08,3.974611,3.986535,3.926915,3.934865,2.179214,10638418
                """;
    }

    private List<Price> getMockData(String shareClassId) {

        var list = new ArrayList<Price>();

        var price = new Price();
        price.setKey(new ShareClassDailyKey());
        price.getKey().setShareClassId(shareClassId);
        price.getKey().setAsOfDate(LocalDate.of(2002, 4, 9));
        price.setOpenPrice(new BigDecimal("4.177316"));
        price.setHigh(new BigDecimal("4.324377"));
        price.setLow(new BigDecimal("4.177316"));
        price.setClosePrice(new BigDecimal("4.236935"));
        price.setAdjustedPrice(new BigDecimal("2.346507"));
        price.setVolume(1041885103L);
        list.add(price);

        price = new Price();
        price.setKey(new ShareClassDailyKey());
        price.getKey().setShareClassId(shareClassId);
        price.getKey().setAsOfDate(LocalDate.of(2002, 4, 10));
        price.setOpenPrice(new BigDecimal("4.236935"));
        price.setHigh(new BigDecimal("4.252834"));
        price.setLow(new BigDecimal("4.129621"));
        price.setClosePrice(new BigDecimal("4.213087"));
        price.setAdjustedPrice(new BigDecimal("2.333299"));
        price.setVolume(170948728L);
        list.add(price);

        price = new Price();
        price.setKey(new ShareClassDailyKey());
        price.getKey().setShareClassId(shareClassId);
        price.getKey().setAsOfDate(LocalDate.of(2002, 5, 8));
        price.setOpenPrice(new BigDecimal("3.974611"));
        price.setHigh(new BigDecimal("3.986535"));
        price.setLow(new BigDecimal("3.926915"));
        price.setClosePrice(new BigDecimal("3.934865"));
        price.setAdjustedPrice(new BigDecimal("2.179214"));
        price.setVolume(10638418);
        list.add(price);

        return list;
    }
}