package com.wedding.serviceapi.exception;

import org.springframework.security.core.AuthenticationException;

public class NotValidPasswordException extends AuthenticationException {
    public NotValidPasswordException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public NotValidPasswordException(String msg) {
        super(msg);
    }
}
