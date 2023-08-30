package me.fengyj.springdemo.models;

import java.time.LocalDateTime;

public class DailyReturn implements DataEntity<ShareClassDailyKey> {

    private ShareClassDailyKey key;
    private double returnIndex;
    private LocalDateTime createdTime;
    private LocalDateTime lastUpdatedTime;

    public ShareClassDailyKey getKey() {
        return key;
    }

    public void setKey(ShareClassDailyKey key) {
        this.key = key;
    }    

    public double getReturnIndex() {
        return returnIndex;
    }

    public void setReturnIndex(double returnIndex) {
        this.returnIndex = returnIndex;
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
