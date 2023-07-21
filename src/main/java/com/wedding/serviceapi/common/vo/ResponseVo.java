package com.wedding.serviceapi.common.vo;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter @Setter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ResponseVo<T> {
    private Boolean success;
    private Integer status;
    private T data;

    public static <T> ResponseVo<T> ok(T data) {
        return new ResponseVo<T>(true, HttpStatus.OK.value(), data);
    }

    public static <T> ResponseVo<T> accepted() {
        return accepted(null);
    }

    public static <T> ResponseVo<T> accepted(T data) {
        return new ResponseVo<T>(true, HttpStatus.ACCEPTED.value(), data);
    }

    public static <T> ResponseVo<T> created(T data) {
        return new ResponseVo<T>(true, HttpStatus.CREATED.value(), data);
    }
}
