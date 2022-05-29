package com.edwkaitwra.backend.config.exception.handler;

public class CorsNotExistsException extends RuntimeException {
    public CorsNotExistsException() {
        super();
    }

    public CorsNotExistsException(String message) {
        super(message);
    }

    public CorsNotExistsException(String message, Throwable cause) {
        super(message, cause);
    }

    public CorsNotExistsException(Throwable cause) {
        super(cause);
    }

    protected CorsNotExistsException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
