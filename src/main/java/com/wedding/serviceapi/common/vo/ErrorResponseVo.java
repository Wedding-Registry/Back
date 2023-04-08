package com.wedding.serviceapi.common.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
public class ErrorResponseVo {
    private Boolean success;
    private Integer status;
    private String message;
}
