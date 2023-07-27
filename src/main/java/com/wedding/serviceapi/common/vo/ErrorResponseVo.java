package com.wedding.serviceapi.common.vo;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter @Setter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ErrorResponseVo {
    private Boolean success;
    private Integer status;
    private String message;

    public static ErrorResponseVo badRequest(String message) {
        return new ErrorResponseVo(false, HttpStatus.BAD_REQUEST.value(), message);
    }

    public static ErrorResponseVo unAuthorized(String message) {
        return new ErrorResponseVo(false, HttpStatus.UNAUTHORIZED.value(), message);
    }

    public static ErrorResponseVo forbidden(String message) {
        return new ErrorResponseVo(false, HttpStatus.FORBIDDEN.value(), message);
    }

    public static ErrorResponseVo internalError(String message) {
        return new ErrorResponseVo(false, HttpStatus.INTERNAL_SERVER_ERROR.value(), message);
    }
}
