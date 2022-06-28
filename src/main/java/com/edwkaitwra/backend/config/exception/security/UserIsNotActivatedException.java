package com.edwkaitwra.backend.config.exception.security;

public class UserIsNotActivatedException extends RuntimeException {

    public UserIsNotActivatedException() {
    }

    public UserIsNotActivatedException(String message) {
        super(message);
    }

    public UserIsNotActivatedException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserIsNotActivatedException(Throwable cause) {
        super(cause);
    }

    public UserIsNotActivatedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
