package me.fengyj.springdemo.models;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Price implements DataEntity<ShareClassDailyKey> {

    private ShareClassDailyKey key;
    private BigDecimal closePrice;
    private LocalDateTime createdTime;
    private LocalDateTime lastUpdatedTime;

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

    public LocalDateTime getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(LocalDateTime createdTime) {
        this.createdTime = createdTime;
    }

    public LocalDateTime getLastUpdatedTime() {
        return lastUpdatedTime;
    }

    public void setLastUpdatedTime(LocalDateTime lastUpdatedTime) {
        this.lastUpdatedTime = lastUpdatedTime;
    }
}
