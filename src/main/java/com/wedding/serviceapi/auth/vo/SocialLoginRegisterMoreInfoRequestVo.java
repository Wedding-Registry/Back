package com.wedding.serviceapi.auth.vo;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@EqualsAndHashCode
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class SocialLoginRegisterMoreInfoRequestVo {

    @NotBlank(message = "소셜 아이디 값은 필수입니다.")
    private String socialId;
    @NotBlank(message = "사용자 이름 값은 필수입니다.")
    private String name;
    @Email(message = "이메일 형식이 올바르지 않습니다.")
    @NotBlank(message = "사용자 이메일 값은 필수입니다.")
    private String email;
    @NotNull(message = "알림 수신 여부를 체크해 주세요.")
    private Boolean notification;
}
