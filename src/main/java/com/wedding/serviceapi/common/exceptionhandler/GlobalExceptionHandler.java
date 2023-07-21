package com.wedding.serviceapi.common.exceptionhandler;

import com.wedding.serviceapi.common.vo.ErrorResponseVo;
import com.wedding.serviceapi.exception.*;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
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
        return ErrorResponseVo.badRequest(e.getMessage());
    }

    @ExceptionHandler
    public ErrorResponseVo DateTimeExceptionHandler(DateTimeException e) {
        log.error("DateTimeException ", e);
        return ErrorResponseVo.badRequest("날짜, 시간 정보가 정확하지 않습니다.");
    }

    @ExceptionHandler
    public ErrorResponseVo NoSuchPathTypeExceptionHandler(NoSuchPathTypeException e) {
        log.error("NoSuchPathTypeException ", e);
        return ErrorResponseVo.badRequest(e.getMessage());
    }

    @ExceptionHandler
    public ErrorResponseVo NegativePriceExceptionHandler(NegativePriceException e) {
        log.error("NegativePriceException ", e);
        return ErrorResponseVo.badRequest(e.getMessage());
    }

    @ExceptionHandler
    public ErrorResponseVo NoSuchElementExceptionHandler(NoSuchElementException e) {
        log.error("NoSuchElementException ", e);
        return ErrorResponseVo.badRequest(e.getMessage());
    }

    @ExceptionHandler
    public ErrorResponseVo MethodArgumentNotValidExceptionHandler(MethodArgumentNotValidException e) {
        log.error("MethodArgumentNotValidException ", e);
        return ErrorResponseVo.badRequest(e.getBindingResult().getAllErrors().get(0).getDefaultMessage());
    }

    @ExceptionHandler
    public ErrorResponseVo NotSamePasswordExceptionHandler(NotSamePasswordException e) {
        log.error("NotSamePasswordException ", e);
        return ErrorResponseVo.badRequest(e.getMessage());
    }

    @ExceptionHandler
    public ErrorResponseVo AlreadyExistedUserException(AlreadyExistedUserException e) {
        log.error("AlreadyExistedUserException ", e);
        return ErrorResponseVo.badRequest(e.getMessage());
    }

    @ExceptionHandler
    public ErrorResponseVo SignatureException(SignatureException e) {
        log.error("SignatureException ", e);
        return ErrorResponseVo.badRequest(e.getMessage());
    }

    @ExceptionHandler
    public ErrorResponseVo InvalidSocialIdException(InvalidSocialPasswordException e) {
        log.error("InvalidSocialIdException ", e);
        return ErrorResponseVo.badRequest(e.getMessage());
    }

    @ExceptionHandler
    public ErrorResponseVo HttpMessageNotReadableException(HttpMessageNotReadableException e) {
        log.error("HttpMessageNotReadableException ", e);
        return ErrorResponseVo.badRequest("필요한 데이터가 없습니다.");
    }

    @ExceptionHandler
    public ErrorResponseVo MissingServletRequestParameterException(MissingServletRequestParameterException e) {
        log.error("MissingServletRequestParameterException ", e);
        String message = e.getParameterName() + " 값이 없습니다.";
        return ErrorResponseVo.badRequest(message);
    }

    @ExceptionHandler
    public ErrorResponseVo NoGuestBoardsInfoJwtExistException(NoGuestBoardsInfoJwtExistException e) {
        log.error("NoGuestBoardsInfoJwtExistException ", e);
        return ErrorResponseVo.badRequest(e.getMessage());
    }
}
