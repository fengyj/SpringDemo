package me.fengyj.springdemo.models;

public interface DataEntity<K> {
    
    K getKey();
    void setKey(K key);
}
