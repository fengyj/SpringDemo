package me.fengyj.springdemo.service.share.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Component
@FeignClient(name = "YahooFinancialAPI")
public interface YahooFinancialService {

    /**
     * Invoke Yahoo financial data API to get price historical data
     * <p>
     * GET <a href="https://query1.finance.yahoo.com/v7/finance/download/600036
     * .SS?period1=923616000&period2=1699228800&interval=1d&events=history&includeAdjustedClose=true">...</a>
     * </p>
     *
     * @param symbol IBM or 600036.SS
     * @param from
     * @param to
     * @param interval
     * @param events
     * @param includeAdjustedClose
     *
     * @return
     */
    @GetMapping("/download/{symbol}")
    String getHistoryPrices(
            @PathVariable("symbol") String symbol,
            @RequestParam(name = "period1", defaultValue = "-2208988800") long from,
            @RequestParam(name = "period2") long to,
            @RequestParam(name = "interval", defaultValue = "1d") String interval,
            @RequestParam(name = "events", defaultValue = "history") String events,
            @RequestParam(name = "includeAdjustedClose", defaultValue = "true") Boolean includeAdjustedClose);

    default String getHistoryPrices(String symbol) {

        return getHistoryPrices(
                symbol,
                -2208988800L,
                LocalDateTime.now().toEpochSecond(ZoneOffset.UTC),
                "1d",
                "history",
                true);
    }
}
