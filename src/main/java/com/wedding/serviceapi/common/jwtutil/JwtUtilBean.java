package com.wedding.serviceapi.common.jwtutil;

import com.wedding.serviceapi.users.domain.Role;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Base64;
import java.util.List;

@Component
public abstract class JwtUtilBean<T> {

    @Value("${jwt.secret.key}")
    String secret;
    @Value("${jwt.secret.access-valid-time}")
    Long accessTokenValidTime;
    @Value("${jwt.secret.refresh-valid-time}")
    Long refreshTokenValidTime;

    public abstract String makeGuestInfoJwt(Long boardsId, Long usersId);

    public abstract List<String> makeAccessTokenAndRefreshToken(Long userId, String userName, Long boardsId, Role role);

    public abstract T decodeJwt(String jwt);

    protected Key makeKey() {
        byte[] keyBytes = Base64.getDecoder().decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
