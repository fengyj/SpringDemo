package me.fengyj.springdemo.service.share.configs;

import me.fengyj.springdemo.utils.exceptions.ExceptionDetail;
import me.fengyj.springdemo.utils.exceptions.ResourceNotFoundException;
import me.fengyj.springdemo.utils.exceptions.UserInvalidInputException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionHandlerAdvice {

    @ExceptionHandler(UserInvalidInputException.class)
    public ProblemDetail handleUserInvalidInputException(UserInvalidInputException ex) {

        ProblemDetail detail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getMessage());
        detail.setProperty("rawInput", ex.getRawInput());
        return detail;
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ProblemDetail handleResourceNotFoundException(ResourceNotFoundException ex) {

        ProblemDetail detail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
        detail.setProperty("resourceId", ex.getResourceId());
        detail.setProperty("resourceType", ex.getResourceType());
        return detail;
    }

    @ExceptionHandler(Exception.class)
    public ProblemDetail handleUnknownException(Exception ex) {

        ProblemDetail detail = ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
        detail.setProperty("exception", ExceptionDetail.create(ex));
        return detail;
    }
}
