package com.wedding.serviceapi.exception;

public class NotSamePasswordException extends RuntimeException {

    public NotSamePasswordException(String message) {
        super(message);
    }
}
