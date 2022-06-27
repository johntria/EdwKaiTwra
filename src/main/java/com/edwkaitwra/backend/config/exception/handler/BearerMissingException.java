package com.edwkaitwra.backend.config.exception.handler;

public class BearerMissingException extends RuntimeException {
    public BearerMissingException() {
        super();
    }

    public BearerMissingException(String message) {
        super(message);
    }

    public BearerMissingException(String message, Throwable cause) {
        super(message, cause);
    }

    public BearerMissingException(Throwable cause) {
        super(cause);
    }

    protected BearerMissingException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
