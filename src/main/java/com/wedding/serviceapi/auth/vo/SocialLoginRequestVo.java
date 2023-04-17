package com.wedding.serviceapi.auth.vo;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@EqualsAndHashCode
@NoArgsConstructor
public class SocialLoginRequestVo implements LoginRequestVo {
    @NotBlank(message = "소셜 아이디가 필요합니다.")
    private String socialId;

    @Override
    public String getSocialIdOrEmail() {
        return socialId;
    }

    @Override
    public String getSocialId() {
        return socialId;
    }
}
