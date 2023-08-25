package me.fengyj.springdemo.models;

import java.time.LocalDate;
import java.util.Objects;

public class ShareClassDailyKey implements Comparable<ShareClassDailyKey> {

    private String shareClassId;
    private LocalDate asOfDate;

    public String getShareClassId() {
        return shareClassId;
    }

    public void setShareClassId(String shareClassId) {
        this.shareClassId = shareClassId;
    }

    public LocalDate getAsOfDate() {
        return asOfDate;
    }

    public void setAsOfDate(LocalDate asOfDate) {
        this.asOfDate = asOfDate;
    }

    @Override
    public int hashCode() {

        return Objects.hash(this.shareClassId, this.asOfDate);
    }

    @Override
    public boolean equals(Object obj) {

        if (!(obj instanceof ShareClassDailyKey))
            return false;
        ShareClassDailyKey anotherObj = (ShareClassDailyKey) obj;
        return compareTo(anotherObj) == 0;
    }

    @Override
    public int compareTo(ShareClassDailyKey o) {

        int result = this.shareClassId.compareTo(o.shareClassId);
        if (result != 0)
            return result;

        result = this.asOfDate.compareTo(o.asOfDate);
        return result;
    }

}
