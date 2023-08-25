package me.fengyj.springdemo.models;

public class DailyReturn implements DataEntity<ShareClassDailyKey> {

    private ShareClassDailyKey key;
    private double returnIndex;

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
}
