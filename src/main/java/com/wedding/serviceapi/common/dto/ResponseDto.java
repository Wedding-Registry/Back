package com.wedding.serviceapi.common.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ResponseDto<T> {
    private Boolean success;
    private Integer status;
    private T data;
}
