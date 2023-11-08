package me.fengyj.springdemo.utils.exceptions;

import me.fengyj.common.exceptions.ApplicationBaseException;
import me.fengyj.common.exceptions.ErrorSeverity;

public class DataIssueException extends ApplicationBaseException {

    private final Class<?> modelClazz;
    private final Object key;
    private final String field;

    public DataIssueException(ErrorSeverity level, Class<?> modelClazz, Object key, String field, String message, Throwable causedBy) {
        super(level, message, causedBy);
        this.modelClazz = modelClazz;
        this.key = key;
        this.field = field;
    }

    public DataIssueException(ErrorSeverity level, Class<?> modelClazz, Object key, String field, String message) {
        this(level, modelClazz, key, field, message, null);
    }

    public Class<?> getModelClazz() {
        return modelClazz;
    }

    public Object getKey() {
        return key;
    }

    public String getField() {
        return field;
    }
}
