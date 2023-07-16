package com.wedding.serviceapi.auth.vo;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@EqualsAndHashCode
@NoArgsConstructor
@Getter
@ToString
public class LoginRequestVo {

    @NotNull(message = "이메일이 반드시 필요합니다.")
    @NotBlank(message = "이메일 형식이 아닙니다.")
    @Email(message = "이메일 형식이 아닙니다.")
    private String email;

    @NotNull(message = "비밀번호를 작성해 주세요.")
    @NotBlank(message = "비밀번호를 작성해 주세요.")
    private String password;
}
