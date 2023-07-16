package com.wedding.serviceapi.common.jwtutil;

import com.wedding.serviceapi.common.vo.LoginUserInfoVo;
import com.wedding.serviceapi.users.domain.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;

@Component
public class AuthJwtUtil extends JwtUtilBean<LoginUserInfoVo> {

//    @Value("${jwt.secret.key}")
//    String secret;
//    @Value("${jwt.secret.access-valid-time}")
//    Long accessTokenValidTime;
//    @Value("${jwt.secret.refresh-valid-time}")
//    Long refreshTokenValidTime;

    @Override
    public String makeGuestInfoJwt(Long boardsId, Long usersId) {
        return null;
    }

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

//    private Key makeKey() {
//        byte[] keyBytes = Base64.getDecoder().decode(secret);
//        return Keys.hmacShaKeyFor(keyBytes);
//    }

    @Override
    public LoginUserInfoVo decodeJwt(String authorizationHeader) {
        Claims claims;
        Key key = makeKey();

        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            throw new IllegalArgumentException("잘못된 토큰 값입니다.");
        }

        String jwt = extractToken(authorizationHeader);
        try {
            claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(jwt).getBody();
        } catch (Exception e) {
            throw new IllegalArgumentException("잘못된 토큰 값입니다.");
        }
        return extractLoginUserInfoDto(claims);
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
