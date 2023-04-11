package com.wedding.serviceapi.auth.jwtutil;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

@Slf4j
public class JwtUtilTest {

    JwtUtilBean jwtUtil;

    @BeforeEach
    void setJwtUtilTest() {
        jwtUtil = new JwtUtilForTest();
    }

    @Test
    @DisplayName("jwt accessToken 만들기")
    void makeAccessToken() {
        // given
        Long userId = 1L;
        String userName = "test";
        // when
        String jwt = jwtUtil.makeAccessToken(userId, userName);
        log.info("jwt = {}", jwt);
        // thenz
        assertThat(jwt).isNotNull();
    }

    @Test
    @DisplayName("jwt refreshToken 만들기")
    void makeRefreshToken() {
        // given
        Long userId = 1L;
        String userName = "test";
        // when
        String jwt = jwtUtil.makeRefreshToken(userId, userName);
        log.info("jwt = {}", jwt);
        // then
        assertThat(jwt).isNotNull();
    }
}
