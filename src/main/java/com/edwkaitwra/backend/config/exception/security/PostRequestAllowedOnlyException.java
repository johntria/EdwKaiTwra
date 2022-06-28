package com.edwkaitwra.backend.config.exception.security;

import org.springframework.security.core.AuthenticationException;

public class PostRequestAllowedOnlyException extends AuthenticationException {
    public PostRequestAllowedOnlyException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public PostRequestAllowedOnlyException(String msg) {
        super(msg);
    }
}
