package com.wedding.serviceapi.auth.controller;

import com.wedding.serviceapi.auth.dto.LoginSuccessDto;
import com.wedding.serviceapi.auth.service.AuthService;
import com.wedding.serviceapi.auth.vo.RegisterUserRequestVo;
import com.wedding.serviceapi.auth.vo.SocialLoginRegisterMoreInfoRequestVo;
import com.wedding.serviceapi.common.vo.ResponseVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@Slf4j
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseVo<LoginSuccessDto> registerUser(@Validated @RequestBody RegisterUserRequestVo body) {
        log.info("registerUser controller body = {}", body);
        LoginSuccessDto data = authService.registerUser(body.getName(), body.getEmail(), body.getPassword(), body.getPasswordCheck(), body.getNotification());

        return new ResponseVo<>(true, HttpStatus.CREATED.value(), data);
    }

    @PostMapping("/social/info")
    public ResponseVo<LoginSuccessDto> registerSocialUser(@Validated @RequestBody SocialLoginRegisterMoreInfoRequestVo body) {
        log.info("registerSocialUser controller body = {}", body);
        LoginSuccessDto data = authService.registerSocialUser(body.getSocialId(), body.getName(), body.getEmail(), body.getNotification());

        return new ResponseVo<>(true, HttpStatus.CREATED.value(), data);
    }
}
