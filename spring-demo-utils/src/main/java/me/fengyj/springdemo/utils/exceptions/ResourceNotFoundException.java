package me.fengyj.springdemo.utils.exceptions;

import me.fengyj.common.exceptions.ApplicationBaseException;
import me.fengyj.common.exceptions.ErrorSeverity;

public class ResourceNotFoundException extends ApplicationBaseException {

    private final Class<?> resourceType;
    private final String resourceId;

    public ResourceNotFoundException(ErrorSeverity level, Class<?> resourceType, String resourceId, String message) {
        super(level, message, null);
        this.resourceType = resourceType;
        this.resourceId = resourceId;
    }

    public Class getResourceType() {
        return resourceType;
    }

    public String getResourceId() {
        return resourceId;
    }
}
