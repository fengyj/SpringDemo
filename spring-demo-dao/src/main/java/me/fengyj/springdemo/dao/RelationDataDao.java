package me.fengyj.springdemo.dao;

import me.fengyj.springdemo.models.DataEntity;

import java.util.List;

public interface RelationDataDao<K extends Comparable<K>, T extends DataEntity<K>> {

    T getByKey(K key);

    void save(T data);

    boolean delete(K key);

    List<T> getAll();
}
