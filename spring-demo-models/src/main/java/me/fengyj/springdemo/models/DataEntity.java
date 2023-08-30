package me.fengyj.springdemo.models;

import java.time.LocalDateTime;

public interface DataEntity<K> {
    
    K getKey();
    void setKey(K key);

    LocalDateTime getCreatedTime();

    void setCreatedTime(LocalDateTime createdTime);

    LocalDateTime getLastUpdatedTime();

    void setLastUpdatedTime(LocalDateTime lastUpdatedTime);
}
