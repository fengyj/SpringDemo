package me.fengyj.springdemo.dao.file;

import me.fengyj.common.exceptions.ApplicationBaseException;
import me.fengyj.common.exceptions.ErrorSeverity;
import me.fengyj.common.exceptions.ResourceInfo;
import org.slf4j.spi.LoggingEventBuilder;

import java.nio.file.Path;

public class FileAccessException extends ApplicationBaseException {

    static final long serialVersionUID = -239134547;
    protected final ResourceInfo resourceInfo;
    protected final Path filePath;

    public FileAccessException(
            ErrorSeverity level,
            Path filePath,
            String message,
            Throwable causedBy) {

        super(level, message, causedBy);
        this.filePath = filePath;
        this.resourceInfo = new ResourceInfo("File", filePath.toString());
    }

    public static FileAccessException create(
            ErrorSeverity level,
            Path filePath,
            String message,
            Throwable causedBy) {

        return new FileAccessException(level, filePath, message, causedBy);
    }

    public Path getFilePath() {

        return filePath;
    }

    @Override
    protected LoggingEventBuilder appendLogData(LoggingEventBuilder builder) {

        return super.appendLogData(builder)
                .addKeyValue("resource_type", resourceInfo.type())
                .addKeyValue("resource_name", resourceInfo.name());
    }
}
