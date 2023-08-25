package me.fengyj.springdemo.dao.file;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "application.datafile")
public class DataFileProperties {

    /**
     * When using data file to save the data, need to specific the serde class for serialization and deserialization.
     */
    private String serde_type = "DataFileJsonSerDe";
    /**
     * When using data file to save the data, need to specific the accessor class for loading/updating the file.
     */
    private String accessor_type = "MemoryFileAccessor";
    /**
     * When using data file to save the data, need to specific the root path for the file.
     */
    @Value("${application.datafile.root-dir:${java.io.tmpdir}}")
    private String root_dir;
    /**
     * When using data file to save the data, need to specific the file extension.
     */
    private String extension = ".json";

    public String getSerde_type() {
        return serde_type;
    }

    public void setSerde_type(String serde_type) {
        this.serde_type = serde_type;
    }

    public String getAccessor_type() {
        return accessor_type;
    }

    public void setAccessor_type(String accessor_type) {
        this.accessor_type = accessor_type;
    }

    public String getRoot_dir() {
        return root_dir;
    }

    public void setRoot_dir(String root_dir) {
        this.root_dir = root_dir;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }
}
