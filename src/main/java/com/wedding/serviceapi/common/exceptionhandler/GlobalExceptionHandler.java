package com.wedding.serviceapi.common.exceptionhandler;

import com.wedding.serviceapi.common.vo.ErrorResponseVo;
import com.wedding.serviceapi.exception.NegativePriceException;
import com.wedding.serviceapi.exception.NoSuchPathTypeException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.DateTimeException;
import java.util.NoSuchElementException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler
    public ErrorResponseVo illegalExceptionHandler(IllegalArgumentException e) {
        log.error("IllegalArgumentException ", e);
        return new ErrorResponseVo(false, HttpStatus.BAD_REQUEST.value(), e.getMessage());
    }

    @ExceptionHandler
    public ErrorResponseVo DateTimeExceptionHandler(DateTimeException e) {
        log.error("DateTimeException ", e);
        return new ErrorResponseVo(false, HttpStatus.BAD_REQUEST.value(), "날짜, 시간 정보가 정확하지 않습니다.");
    }

    @ExceptionHandler
    public ErrorResponseVo NoSuchPathTypeExceptionHandler(NoSuchPathTypeException e) {
        log.error("NoSuchPathTypeException ", e);
        return new ErrorResponseVo(false, HttpStatus.BAD_REQUEST.value(), e.getMessage());
    }

    @ExceptionHandler
    public ErrorResponseVo NegativePriceExceptionHandler(NegativePriceException e) {
        log.error("NegativePriceException ", e);
        return new ErrorResponseVo(false, HttpStatus.BAD_REQUEST.value(), e.getMessage());
    }

    @ExceptionHandler
    public ErrorResponseVo NoSuchElementExceptionHandler(NoSuchElementException e) {
        log.error("NoSuchElementException ", e);
        return new ErrorResponseVo(false, HttpStatus.BAD_REQUEST.value(), e.getMessage());
    }

}
