package com.wedding.serviceapi.auth.jwtutil;

import com.wedding.serviceapi.common.vo.LoginUserInfoVo;

import java.util.List;

public interface JwtUtilBean {

    List<String> makeAccessTokenAndRefreshToken(Long userId, String userName);

    LoginUserInfoVo decodeJwt(String jwt);
}
