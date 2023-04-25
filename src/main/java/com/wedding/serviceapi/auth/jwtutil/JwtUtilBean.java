package com.wedding.serviceapi.auth.jwtutil;

import com.wedding.serviceapi.common.vo.LoginUserInfoVo;
import com.wedding.serviceapi.users.domain.Role;

import java.util.List;

public interface JwtUtilBean {

    List<String> makeAccessTokenAndRefreshToken(Long userId, String userName, Role role);

    LoginUserInfoVo decodeJwt(String jwt);
}
