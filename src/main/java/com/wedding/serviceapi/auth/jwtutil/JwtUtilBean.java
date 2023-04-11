package com.wedding.serviceapi.auth.jwtutil;

public interface JwtUtilBean {

    String makeAccessToken(Long userId, String userName);

    String makeRefreshToken(Long userId, String userName);
}
