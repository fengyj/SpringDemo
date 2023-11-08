package me.fengyj.springdemo.models;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Price implements DataEntity<ShareClassDailyKey> {

    private ShareClassDailyKey key;
    private BigDecimal openPrice;
    private BigDecimal closePrice;
    private BigDecimal high;
    private BigDecimal low;
    private BigDecimal adjustedPrice;
    private long volume;
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

    public BigDecimal getOpenPrice() {
        return openPrice;
    }

    public void setOpenPrice(BigDecimal openPrice) {
        this.openPrice = openPrice;
    }

    public BigDecimal getHigh() {
        return high;
    }

    public void setHigh(BigDecimal high) {
        this.high = high;
    }

    public BigDecimal getLow() {
        return low;
    }

    public void setLow(BigDecimal low) {
        this.low = low;
    }

    public BigDecimal getAdjustedPrice() {
        return adjustedPrice;
    }

    public void setAdjustedPrice(BigDecimal adjustedPrice) {
        this.adjustedPrice = adjustedPrice;
    }

    public long getVolume() {
        return volume;
    }

    public void setVolume(long volume) {
        this.volume = volume;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Price price = (Price) o;

        if (volume != price.volume) return false;
        if (!key.equals(price.key)) return false;
        if (!openPrice.equals(price.openPrice)) return false;
        if (!closePrice.equals(price.closePrice)) return false;
        if (!high.equals(price.high)) return false;
        if (!low.equals(price.low)) return false;
        return adjustedPrice.equals(price.adjustedPrice);
    }

    @Override
    public int hashCode() {
        return key.hashCode();
    }
}
