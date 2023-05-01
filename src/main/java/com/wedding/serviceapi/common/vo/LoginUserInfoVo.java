package com.wedding.serviceapi.common.vo;

import com.wedding.serviceapi.users.domain.Role;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@EqualsAndHashCode
@Getter
@AllArgsConstructor
public class LoginUserInfoVo {
    private Long userId;
    private String userName;
    private Long boardsId;
    private Role role;
}
