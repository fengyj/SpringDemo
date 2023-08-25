package me.fengyj.springdemo.dao.file;

import me.fengyj.springdemo.models.DataEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.TreeMap;

@Repository
@EnableConfigurationProperties(DataFileProperties.class)
public abstract class DataFileDao<K extends Comparable<K>, T extends DataEntity<K>> {

    protected final String category;
    protected final Class<T> entityClazz;
    @Autowired
    private List<DataFileSerDe> serDes;
    @Autowired
    private List<FileAccessor> accessors;
    @Autowired
    private DataFileProperties properties;
//    @Value("${application.data_file.serde_type:DataFileJsonSerDe}")
//    private String selectedDataFileSerDeType;
//    @Value("${application.data_file.accessor_type:MemoryFileAccessor}")
//    private String selectedDataFileAccessorType;
    private DataFileSerDe selectedSerDe;
    private FileAccessor selectedAccessor;

    protected DataFileDao(String category, Class<T> entityClazz) {

        this.category = category;
        this.entityClazz = entityClazz;
    }

    public DataFileSerDe getSerDe() {

        if (this.selectedSerDe == null
                || !this.selectedSerDe.getClass().getSimpleName().equalsIgnoreCase(properties.getSerde_type())) {
            this.selectedSerDe = this.serDes.stream()
                    .filter(i -> i.getClass().getSimpleName().equalsIgnoreCase(properties.getSerde_type())).findAny()
                    .orElseThrow(() -> new IllegalArgumentException("Cannot find the DataFileSerDe class by the given name: " + properties.getSerde_type()));
        }
        return this.selectedSerDe;
    }

    public FileAccessor getAccessor() {

        if (this.selectedAccessor == null
                || !this.selectedAccessor.getClass().getSimpleName().equalsIgnoreCase(properties.getAccessor_type())) {
            this.selectedAccessor = this.accessors.stream()
                    .filter(i -> i.getClass().getSimpleName().equalsIgnoreCase(properties.getAccessor_type())).findAny()
                    .orElseThrow(() -> new IllegalArgumentException("Cannot find the FileAccessor class by the given name: " + properties.getAccessor_type()));
        }
        return this.selectedAccessor;
    }

    public TreeMap<K, T> getList(String partitionKey) {

        TreeMap<K, T> map = new TreeMap<>();

        var bytes = getAccessor().downloadFromFile(category, partitionKey);
        var list = getSerDe().deserialize(bytes, entityClazz);

        if (list != null) {
            list.forEach(i -> map.put(i.getKey(), i));
        }

        return map;
    }

    public void save(String partitionKey, TreeMap<K, T> historicalList) {

        var bytes = getSerDe().serialize(historicalList.values());

        getAccessor().uploadToFile(category, partitionKey, bytes);
    }
}
