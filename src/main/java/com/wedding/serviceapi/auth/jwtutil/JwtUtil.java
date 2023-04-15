package com.wedding.serviceapi.auth.jwtutil;

import com.wedding.serviceapi.common.vo.LoginUserInfoVo;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;

@Component
public class JwtUtil implements JwtUtilBean {

    @Value("${jwt.secret.key}")
    String secret;
    @Value("${jwt.secret.access-valid-time}")
    Long accessTokenValidTime;
    @Value("${jwt.secret.refresh-valid-time}")
    Long refreshTokenValidTime;

    @Override
    public ArrayList<String> makeAccessTokenAndRefreshToken(Long userId, String userName) {
        ArrayList<String> tokenList = new ArrayList<>();
        tokenList.add(makeAccessToken(userId, userName));
        tokenList.add(makeRefreshToken(userId, userName));
        return tokenList;
    }

    private String makeAccessToken(Long userId, String userName) {
        Key key = makeKey();
        Date now = new Date();
        return Jwts.builder().setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + accessTokenValidTime))
                .claim("userId", userId)
                .claim("userName", userName)
                .signWith(key)
                .compact();
    }

    private String makeRefreshToken(Long userId, String userName) {
        Key key = makeKey();
        Date now = new Date();
        return Jwts.builder().setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + refreshTokenValidTime))
                .claim("userId", userId)
                .claim("userName", userName)
                .signWith(key)
                .compact();
    }

    private Key makeKey() {
        byte[] keyBytes = Base64.getDecoder().decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    @Override
    public LoginUserInfoVo decodeJwt(String authorizationHeader) {
        Key key = makeKey();

        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            throw new IllegalArgumentException("잘못된 토큰 값입니다.");
        }

        String jwt = extractToken(authorizationHeader);

        Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(jwt).getBody();
        return extractLoginUserInfoDto(claims);
    }

    private String extractToken(String authorizationHeader) {
        return authorizationHeader.substring(7);
    }

    private LoginUserInfoVo extractLoginUserInfoDto(Claims body) {
        Long userId = body.get("userId", Long.class);
        String userName = body.get("userName", String.class);
        return new LoginUserInfoVo(userId, userName);
    }
}
