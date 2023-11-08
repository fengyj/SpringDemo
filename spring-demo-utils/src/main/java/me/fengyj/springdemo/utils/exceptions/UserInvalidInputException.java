package me.fengyj.springdemo.utils.exceptions;

import me.fengyj.common.exceptions.ApplicationBaseException;
import me.fengyj.common.exceptions.ErrorSeverity;

public class UserInvalidInputException extends ApplicationBaseException {

    private final Object rawInput;

    public UserInvalidInputException(ErrorSeverity level, Object rawInput, String message, Throwable causedBy) {
        super(level, message, causedBy);
        this.rawInput = rawInput;
    }

    public Object getRawInput() {
        return rawInput;
    }
}
