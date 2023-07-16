package com.wedding.serviceapi.exception;

import org.springframework.security.core.AuthenticationException;

public class InvalidSocialPasswordException extends AuthenticationException {
    public InvalidSocialPasswordException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public InvalidSocialPasswordException(String msg) {
        super(msg);
    }
}
