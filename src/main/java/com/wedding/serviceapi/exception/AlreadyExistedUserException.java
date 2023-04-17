package com.wedding.serviceapi.exception;

public class AlreadyExistedUserException extends RuntimeException{

    public AlreadyExistedUserException(String message) {
        super(message);
    }
}
