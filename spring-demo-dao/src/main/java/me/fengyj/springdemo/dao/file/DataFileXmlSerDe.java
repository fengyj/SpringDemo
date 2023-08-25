package me.fengyj.springdemo.dao.file;

import me.fengyj.common.utils.XmlConverter;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
public class DataFileXmlSerDe implements DataFileSerDe {

    @Override
    public <T> byte[] serialize(Collection<T> data) {
        
        return XmlConverter.toBytes(data);
    }

    @Override
    public <T> Collection<T> deserialize(byte[] bytes, Class<T> clazz) {
        
        return XmlConverter.fromBytesToList(bytes, clazz);
    }
    
}
