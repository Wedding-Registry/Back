package com.wedding.serviceapi.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
public class LoginSuccessDto {
    private Long userId;
    private String userName;
    private Long boardsId;
    private String accessToken;
    private String refreshToken;
    private Boolean needMoreInfo;
}
