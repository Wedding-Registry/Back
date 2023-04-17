package com.wedding.serviceapi.auth.jwtutil;

import com.wedding.serviceapi.common.vo.LoginUserInfoVo;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.security.InvalidKeyException;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@Slf4j
public class JwtUtilTest {

    JwtUtilBean jwtUtil;

    private Long userId;
    private String userName;

    @BeforeEach
    void init() {
        userId = 1L;
        userName = "test";
    }

    @BeforeEach
    void setJwtUtilTest() {
        jwtUtil = new JwtUtilForTest();
    }

    @Test
    @DisplayName("token 생성 테스트")
    void makeTokens() {
        // when
        List<String> tokenList = jwtUtil.makeAccessTokenAndRefreshToken(userId, userName);
        // then
        assertThat(tokenList.size()).isEqualTo(2);
        assertThat(tokenList.get(0)).isNotNull();
        assertThat(tokenList.get(1)).isNotNull();

    }

//    @Test
//    @DisplayName("jwt accessToken 만들기")
//    void makeAccessToken() {
//        // when
//        String jwt = jwtUtil.makeAccessToken(userId, userName);
//        log.info("jwt = {}", jwt);
//        // thenz
//        assertThat(jwt).isNotNull();
//    }
//
//    @Test
//    @DisplayName("jwt refreshToken 만들기")
//    void makeRefreshToken() {
//        // when
//        String jwt = jwtUtil.makeRefreshToken(userId, userName);
//        log.info("jwt = {}", jwt);
//        // then
//        assertThat(jwt).isNotNull();
//    }

    @ParameterizedTest
    @ValueSource(strings = {"Bearer", "bearer", "bear"})
    @DisplayName("Bearer token 형식이 아닌 경우 에러")
    void validBearerToken(String bearer) {
        List<String> tokenList = jwtUtil.makeAccessTokenAndRefreshToken(userId, userName);
        String bearerJwt = bearer + tokenList.get(0);
        // when
        assertThrows(IllegalArgumentException.class, () -> jwtUtil.decodeJwt(bearerJwt));
    }

    @Test
    @DisplayName("authorization header 값이 없는 경우")
    void noAuthorizationHeader() {
        // given
        String header = null;
        // when
        assertThrows(IllegalArgumentException.class, () -> jwtUtil.decodeJwt(header));
    }

    @Test
    @DisplayName("jwt값이 유효하지 않은 경우")
    void invalidJwt() {
        // given
        String jwt = "eyJhbGciOiJIUzUxMiJ9.eyJpYXQiOjE2ODE0MDY0MDgsImV4cCI6MTcxMjk0MjQwOCwidXNlcklkIjoxLCJ1c2VyTmFtZSI6InRlc3QifQ.qtU2Z2frD0icHr2VE0Tekvue1wJtyrJHF44Qfhb7f29YKHF29C8HdolyxiUi3iF6RhYrJNNcd-JTo4O3iPVPV";
        String header = "Bearer " + jwt;
        // when
        assertThrows(SignatureException.class, () -> jwtUtil.decodeJwt(header));
        // then
    }

    @Test
    @DisplayName("jwt token 해석")
    void decodeJwt() {
        // given
        List<String> tokenList = jwtUtil.makeAccessTokenAndRefreshToken(userId, userName);
        String token = "Bearer " + tokenList.get(0);
        // when
        LoginUserInfoVo userInfo = jwtUtil.decodeJwt(token);
        // then
        assertThat(userInfo.getUserId()).isEqualTo(1L);
        assertThat(userInfo.getUserName()).isEqualTo("test");
    }
}






















