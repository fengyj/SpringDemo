package me.fengyj.springdemo.service.share.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class YahooFinancialServiceIT {

    @Autowired
    private YahooFinancialService svc;

    @Test
    public void test_getHistoryPrice() {

        var response = svc.getHistoryPrices("600036.SS");

        Assertions.assertNotNull(response);
        Assertions.assertTrue(response.startsWith("Date,Open,High,Low,Close,Adj Close,Volume\n"));
    }
}