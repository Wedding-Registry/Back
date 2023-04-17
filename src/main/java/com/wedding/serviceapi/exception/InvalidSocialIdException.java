package com.wedding.serviceapi.exception;

import org.springframework.security.core.AuthenticationException;

public class InvalidSocialIdException extends AuthenticationException {
    public InvalidSocialIdException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public InvalidSocialIdException(String msg) {
        super(msg);
    }
}
