package com.wedding.serviceapi.auth.vo;

import com.wedding.serviceapi.users.domain.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginUserVo {
    private Long userId;
    private String userName;
    private Role role;
}
