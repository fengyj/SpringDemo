package me.fengyj.springdemo.dao.file;

import me.fengyj.common.utils.JsonConverter;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
public class DataFileJsonSerDe implements DataFileSerDe {
    
    public <T> byte[] serialize(Collection<T> data) {

        return JsonConverter.toBytes(data);
    }

    public <T> Collection<T> deserialize(byte[] bytes, Class<T> clazz) {

        return JsonConverter.fromBytesToList(bytes, clazz);
    }
}
