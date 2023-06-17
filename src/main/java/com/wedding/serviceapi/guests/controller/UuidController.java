package com.wedding.serviceapi.guests.controller;

import com.wedding.serviceapi.auth.vo.LoginUser;
import com.wedding.serviceapi.auth.vo.LoginUserVo;
import com.wedding.serviceapi.common.vo.ResponseVo;
import com.wedding.serviceapi.guests.dto.UuidResponseDto;
import com.wedding.serviceapi.guests.service.UuidService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @PostMapping
    public ResponseVo<Void> postBoardsIdCookie(@LoginUser LoginUserVo loginUserVo) {
        log.info("[postBoardsIdCookie controller] usersId = {}", loginUserVo.getUserId());
        return null;
    }
}





















