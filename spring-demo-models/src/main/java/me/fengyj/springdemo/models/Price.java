package me.fengyj.springdemo.models;

import java.math.BigDecimal;

public class Price implements DataEntity<ShareClassDailyKey> {

    private ShareClassDailyKey key;
    private BigDecimal closePrice;

    public ShareClassDailyKey getKey() {
        return key;
    }

    public void setKey(ShareClassDailyKey key) {
        this.key = key;
    }    

    public BigDecimal getClosePrice() {
        return closePrice;
    }

    public void setClosePrice(BigDecimal closePrice) {
        this.closePrice = closePrice;
    }

}
