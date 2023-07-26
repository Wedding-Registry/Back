package com.wedding.serviceapi.exception;

public class S3ObjectException extends RuntimeException {
    public S3ObjectException(String message, Throwable cause) {
        super(message, cause);
    }
}
