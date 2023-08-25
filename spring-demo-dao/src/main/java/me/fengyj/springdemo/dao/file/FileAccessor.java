package me.fengyj.springdemo.dao.file;

import me.fengyj.common.exceptions.ErrorSeverity;
import me.fengyj.common.utils.IOUtils;
import me.fengyj.common.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public abstract class FileAccessor {

    protected final static Logger logger = LoggerFactory.getLogger(FileAccessor.class);

    protected abstract byte[] downloadFromFile(String category, String partitionKey);

    protected abstract void uploadToFile(String category, String partitionKey, byte[] bytes);

    @Component
    public static class MemoryFileAccessor extends FileAccessor {

        private final Map<String, byte[]> files = new HashMap<>();

        @Override
        protected byte[] downloadFromFile(String category, String partitionKey) {

            var key = getKey(category, partitionKey);
            var bytes = files.get(key);
            if (bytes != null) return bytes;

            throw FileAccessException.create(ErrorSeverity.Error, Path.of(key), "Cannot find the data.", null);
        }

        @Override
        protected void uploadToFile(String category, String partitionKey, byte[] bytes) {

            var key = getKey(category, partitionKey);
            if (bytes == null)
                throw FileAccessException.create(ErrorSeverity.Error, Path.of(key), "The content is null.", null);
            files.put(key, bytes);
        }

        public String getKey(String category, String partitionKey) {

            return category + "." + partitionKey;
        }
    }

    @Component
    @EnableConfigurationProperties(DataFileProperties.class)
    public static class LocalFileAccessor extends FileAccessor {
//
//        @Value("${application.data_file.root_dir:${java.io.tmpdir}}")
//        private String rootDir;
//        @Value("${application.data_file.extension:.json}")
//        private String fileExt;
        @Autowired
        private DataFileProperties properties;

        @Override
        protected byte[] downloadFromFile(String category, String partitionKey) {

            var filePath = getFilePath(category, partitionKey);
            try {
                return IOUtils.readFileAsBytes(filePath);
            } catch (IOException ex) {
                throw FileAccessException.create(ErrorSeverity.Error, filePath, "Cannot read the file.", ex);
            }
        }

        @Override
        protected void uploadToFile(String category, String partitionKey, byte[] bytes) {

            var filePath = getFilePath(category, partitionKey);
            try {
                IOUtils.writeFile(filePath, bytes);
                logger.atDebug()
                        .addKeyValue("file_path", filePath)
                        .addKeyValue("file_size", StringUtils.getDataSize(bytes.length))
                        .log("Data has been saved to the file.");
            } catch (IOException ex) {
                throw FileAccessException.create(ErrorSeverity.Error, filePath, "Cannot write the file.", ex);
            }
        }

        public Path getFilePath(String category, String partitionKey) {

            var fileName = partitionKey;
            if (properties.getExtension() != null) fileName = fileName + properties.getExtension();
            return Path.of(properties.getRoot_dir(), category, fileName);
        }
    }
}
