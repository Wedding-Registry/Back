package com.wedding.serviceapi.auth.jwtutil;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtUtil implements JwtUtilBean {

    @Value("${jwt.secret.key}")
    String secret;
    @Value("${jwt.secret.access-valid-time}")
    Long accessTokenValidTime;
    @Value("${jwt.secret.refresh-valid-time}")
    Long refreshTokenValidTime;

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
