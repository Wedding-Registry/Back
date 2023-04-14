package com.wedding.serviceapi.common.vo;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@EqualsAndHashCode
@Getter
@AllArgsConstructor
public class LoginUserInfoVo {
    private Long userId;
    private String userName;
}
