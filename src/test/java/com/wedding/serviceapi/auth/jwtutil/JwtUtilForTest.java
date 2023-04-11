package com.wedding.serviceapi.auth.jwtutil;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.util.Base64;
import java.util.Date;

public class JwtUtilForTest implements JwtUtilBean {

    String secret = "dGVzdHRlc3R0ZXN0dGVzdHRlc3R0ZXN0dGVzdHRlc3R0ZXN0dGVzdHRlc3R0ZXN0dGVzdHRlc3R0ZXN0dGVzdHRlc3R0ZXN0dGVzdHRlc3R0ZXN0dGVzdHRlc3R0ZXN0dGVzdHRlc3R0ZXN0dGVzdHRlc3R0ZXN0dGVzdHRlc3R0ZXN0dGVzdHRlc3R0ZXN0dGVzdHRlc3R0ZXN0dGVzdHRlc3R0ZXN0dGVzdHRlc3R0ZXN0dGVzdHRlc3R0ZXN0dGVzdHRlc3Q=";
    Long accessTokenValidTime = 86400000L;
    Long refreshTokenValidTime = 31536000000L;

    @Override
    public String makeAccessToken(Long userId, String userName) {
        Key key = makeKey();
        return Jwts.builder().setSubject(String.valueOf(userId))
                .setSubject(userName)
                .signWith(key)
                .setExpiration(new Date(System.currentTimeMillis() + accessTokenValidTime))
                .compact();
    }

    @Override
    public String makeRefreshToken(Long userId, String userName) {
        Key key = makeKey();
        return Jwts.builder().setSubject(String.valueOf(userId))
                .setSubject(userName)
                .signWith(key)
                .setExpiration(new Date(System.currentTimeMillis() + refreshTokenValidTime))
                .compact();
    }

    private Key makeKey() {
        byte[] keyBytes = Base64.getDecoder().decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
