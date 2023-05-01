package com.wedding.serviceapi.auth.jwtutil;

import com.wedding.serviceapi.common.vo.LoginUserInfoVo;
import com.wedding.serviceapi.users.domain.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;

public class JwtUtilForTest implements JwtUtilBean {

    String secret = "dGVzdHRlc3R0ZXN0dGVzdHRlc3R0ZXN0dGVzdHRlc3R0ZXN0dGVzdHRlc3R0ZXN0dGVzdHRlc3R0ZXN0dGVzdHRlc3R0ZXN0dGVzdHRlc3R0ZXN0dGVzdHRlc3R0ZXN0dGVzdHRlc3R0ZXN0dGVzdHRlc3R0ZXN0dGVzdHRlc3R0ZXN0dGVzdHRlc3R0ZXN0dGVzdHRlc3R0ZXN0dGVzdHRlc3R0ZXN0dGVzdHRlc3R0ZXN0dGVzdHRlc3R0ZXN0dGVzdHRlc3Q=";
    Long accessTokenValidTime = 86400000L;
    Long refreshTokenValidTime = 31536000000L;

    @Override
    public ArrayList<String> makeAccessTokenAndRefreshToken(Long userId, String userName, Long boardsId, Role role) {
        ArrayList<String> tokenList = new ArrayList<>();
        tokenList.add(makeAccessToken(userId, userName, boardsId, role));
        tokenList.add(makeRefreshToken(userId, userName, boardsId, role));
        return tokenList;
    }

    private String makeAccessToken(Long userId, String userName, Long boardsId, Role role) {
        Key key = makeKey();
        Date now = new Date();
        return Jwts.builder().setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + accessTokenValidTime))
                .claim("userId", userId)
                .claim("userName", userName)
                .claim("boardsId", boardsId)
                .claim("role", role.name())
                .signWith(key)
                .compact();
    }

    private String makeRefreshToken(Long userId, String userName, Long boardsId, Role role) {
        Key key = makeKey();
        Date now = new Date();
        return Jwts.builder().setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + refreshTokenValidTime))
                .claim("userId", userId)
                .claim("userName", userName)
                .claim("boardsId", boardsId)
                .claim("role", role.name())
                .signWith(key)
                .compact();
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

    private Key makeKey() {
        byte[] keyBytes = Base64.getDecoder().decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private String extractToken(String authorizationHeader) {
        return authorizationHeader.substring(7);
    }

    private LoginUserInfoVo extractLoginUserInfoDto(Claims body) {
        Long userId = body.get("userId", Long.class);
        String userName = body.get("userName", String.class);
        Long boardsId = body.get("boardsId", Long.class);
        String roleString = body.get("role", String.class);
        Role role = Role.fromString(roleString);

        return new LoginUserInfoVo(userId, userName, boardsId, role);
    }
}
