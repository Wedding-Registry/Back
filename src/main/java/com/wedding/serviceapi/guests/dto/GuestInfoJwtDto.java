package com.wedding.serviceapi.guests.dto;

import lombok.Getter;

@Getter
public class GuestInfoJwtDto {
    private String guestInfoJwt;

    private GuestInfoJwtDto(String guestInfoJwt) {
        this.guestInfoJwt = guestInfoJwt;
    }

    public static GuestInfoJwtDto of(String jwt) {
        return new GuestInfoJwtDto(jwt);
    }
}
