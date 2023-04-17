package com.wedding.serviceapi.common.exceptionhandler;

import com.wedding.serviceapi.common.vo.ErrorResponseVo;
import com.wedding.serviceapi.exception.*;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
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

    @ExceptionHandler
    public ErrorResponseVo MethodArgumentNotValidExceptionHandler(MethodArgumentNotValidException e) {
        log.error("MethodArgumentNotValidException ", e);
        return new ErrorResponseVo(false, HttpStatus.BAD_REQUEST.value(), e.getBindingResult().getAllErrors().get(0).getDefaultMessage());
    }

    @ExceptionHandler
    public ErrorResponseVo NotSamePasswordExceptionHandler(NotSamePasswordException e) {
        log.error("NotSamePasswordException ", e);
        return new ErrorResponseVo(false, HttpStatus.BAD_REQUEST.value(), e.getMessage());
    }

    @ExceptionHandler
    public ErrorResponseVo AlreadyExistedUserException(AlreadyExistedUserException e) {
        log.error("AlreadyExistedUserException ", e);
        return new ErrorResponseVo(false, HttpStatus.BAD_REQUEST.value(), e.getMessage());
    }

    @ExceptionHandler
    public ErrorResponseVo SignatureException(SignatureException e) {
        log.error("SignatureException ", e);
        return new ErrorResponseVo(false, HttpStatus.BAD_REQUEST.value(), e.getMessage());
    }

    @ExceptionHandler
    public ErrorResponseVo InvalidSocialIdException(InvalidSocialIdException e) {
        log.error("InvalidSocialIdException ", e);
        return new ErrorResponseVo(false, HttpStatus.BAD_REQUEST.value(), e.getMessage());
    }
}
