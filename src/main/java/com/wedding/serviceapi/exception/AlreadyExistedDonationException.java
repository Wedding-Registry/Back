package com.wedding.serviceapi.exception;

public class AlreadyExistedDonationException extends RuntimeException{

    public AlreadyExistedDonationException(String message) {
        super(message);
    }
}
