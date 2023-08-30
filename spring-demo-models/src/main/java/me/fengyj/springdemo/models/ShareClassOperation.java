package me.fengyj.springdemo.models;

import java.time.LocalDateTime;
import java.util.Objects;

public class ShareClassOperation implements DataEntity<String> {

    private String shareClassId;
    private String name;
    private LocalDateTime createdTime;
    private LocalDateTime lastUpdatedTime;

    @Override
    public String getKey() {
        return shareClassId;
    }

    @Override
    public void setKey(String key) {
        shareClassId = key;
    }

    public String getShareClassId() {
        return shareClassId;
    }

    public void setShareClassId(String shareClassId) {
        this.shareClassId = shareClassId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
        ShareClassOperation that = (ShareClassOperation) o;
        return Objects.equals(shareClassId, that.shareClassId) && Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(shareClassId);
    }
}
