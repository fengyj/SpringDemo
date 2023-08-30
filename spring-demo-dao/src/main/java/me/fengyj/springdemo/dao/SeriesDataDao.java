package me.fengyj.springdemo.dao;


import me.fengyj.springdemo.models.DataEntity;

import java.util.TreeMap;

public interface SeriesDataDao<K, T extends DataEntity<K>> {
    
    TreeMap<K, T> getList(String partitionKey);
    void save(String partitionKey, TreeMap<K, T> historicalList);
}
