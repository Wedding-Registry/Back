package com.wedding.serviceapi.common.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter @Setter
@AllArgsConstructor
public class ResponseDto<T> {
    private Boolean success;
    private Integer status;
    private T data;
}
