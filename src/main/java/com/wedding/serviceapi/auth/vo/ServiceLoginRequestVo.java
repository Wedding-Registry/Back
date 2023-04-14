package com.wedding.serviceapi.auth.vo;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@EqualsAndHashCode
@Getter
@NoArgsConstructor
public class ServiceLoginRequestVo {

    @NotBlank(message = "이메일 형식이 아닙니다.")
    @Email(message = "이메일 형식이 아닙니다.")
    private String email;

    @NotBlank(message = "비밀번호를 작성해 주세요.")
    private String password;
}
