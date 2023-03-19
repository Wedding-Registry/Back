package com.wedding.serviceapi.common.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
public class ResponseVo<T> {
    private Boolean success;
    private Integer status;
    private T data;
}
