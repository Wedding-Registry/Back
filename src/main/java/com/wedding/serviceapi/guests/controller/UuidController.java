package com.wedding.serviceapi.guests.controller;

import com.wedding.serviceapi.auth.vo.LoginUser;
import com.wedding.serviceapi.auth.vo.LoginUserVo;
import com.wedding.serviceapi.common.vo.ResponseVo;
import com.wedding.serviceapi.guests.dto.UuidRequestDto;
import com.wedding.serviceapi.guests.dto.UuidResponseDto;
import com.wedding.serviceapi.guests.service.UuidService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

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
    public ResponseVo<Void> postBoardsIdCookie(@LoginUser LoginUserVo loginUserVo,
                                               @RequestBody UuidRequestDto uuidRequestDto,
                                               HttpServletResponse response
    ) {
        log.info("[postBoardsIdCookie controller] usersId = {}, uuidFirst = {}, uuidSecond = {}",
                loginUserVo.getUserId(), uuidRequestDto.getUuidFirst(), uuidRequestDto.getUuidSecond());
        uuidService.setBoardsIdCookie(uuidRequestDto.getUuidFirst(), uuidRequestDto.getUuidSecond(), response);

        return new ResponseVo<>(true, HttpStatus.OK.value(), null);
    }
}





















