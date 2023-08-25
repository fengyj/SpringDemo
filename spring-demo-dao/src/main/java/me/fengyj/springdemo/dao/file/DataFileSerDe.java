package me.fengyj.springdemo.dao.file;

import java.util.Collection;

public interface DataFileSerDe {
    
    <T> byte[] serialize(Collection<T> data);

    <T> Collection<T> deserialize(byte[] bytes, Class<T> clazz);
}
