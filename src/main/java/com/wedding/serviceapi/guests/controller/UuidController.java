package com.wedding.serviceapi.guests.controller;

import com.wedding.serviceapi.auth.vo.LoginUser;
import com.wedding.serviceapi.auth.vo.LoginUserVo;
import com.wedding.serviceapi.common.vo.ResponseVo;
import com.wedding.serviceapi.guests.dto.GuestInfoJwtDto;
import com.wedding.serviceapi.guests.dto.UuidResponseDto;
import com.wedding.serviceapi.guests.service.UuidService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/invitation/uuids")
public class UuidController {

    private final UuidService uuidService;

    @GetMapping
    public ResponseVo<UuidResponseDto> getUuids(@LoginUser LoginUserVo loginUserVo) {
        log.info("[getUuids controller] usersId = {}, boardsId = {}", loginUserVo.getUserId(), loginUserVo.getBoardsId());
        UuidResponseDto data = uuidService.getUuid(loginUserVo.getBoardsId(), loginUserVo.getUserId());

        return new ResponseVo<>(true, HttpStatus.OK.value(), data);
    }

    @GetMapping("/info")
    public ResponseVo<GuestInfoJwtDto> getJwtAboutGuestInfo(@LoginUser LoginUserVo loginUserVo,
                                                           @RequestParam("uuidFirst") String uuidFirst,
                                                           @RequestParam("uuidSecond") String uuidSecond
    ) {
        log.info("[getJwtAboutGuestInfo controller] usersId = {}, uuidFirst = {}, uuidSecond = {}",
                loginUserVo.getUserId(), uuidFirst, uuidSecond);
        GuestInfoJwtDto data = uuidService.makeGuestInfoJwt(uuidFirst, uuidSecond, loginUserVo.getUserId());

        return new ResponseVo<>(true, HttpStatus.OK.value(), data);
    }
}





















