package com.wedding.serviceapi.auth.jwtutil;

import com.wedding.serviceapi.common.vo.LoginUserInfoVo;

public interface JwtUtilBean {

    String makeAccessToken(Long userId, String userName);

    String makeRefreshToken(Long userId, String userName);

    LoginUserInfoVo decodeJwt(String jwt);
}
