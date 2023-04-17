package com.wedding.serviceapi.auth.vo;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@EqualsAndHashCode
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class RegisterUserRequestVo {

    @NotBlank(message = "이름을 작성해 주세요.")
    private String name;
    @NotBlank(message = "이메일 형식이 아닙니다.")
    @Email(message = "이메일 형식이 아닙니다.")
    private String email;
    @NotBlank(message = "비밀번호를 작성해 주세요.")
    private String password;
    @NotBlank(message = "확인용 비밀번호를 작성해 주세요.")
    private String passwordCheck;
    @NotNull(message = "알림 수신 여부를 체크해 주세요.")
    private Boolean notification;
}
