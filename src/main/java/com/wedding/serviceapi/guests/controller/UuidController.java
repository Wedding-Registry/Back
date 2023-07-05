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

    @PostMapping
    public ResponseVo<Void> postBoardsIdCookie(@LoginUser LoginUserVo loginUserVo,
                                               @Validated @RequestBody UuidRequestDto uuidRequestDto,
                                               HttpServletRequest request,
                                               HttpServletResponse response
    ) {
        log.info("[postBoardsIdCookie controller] usersId = {}, uuidFirst = {}, uuidSecond = {}",
                loginUserVo.getUserId(), uuidRequestDto.getUuidFirst(), uuidRequestDto.getUuidSecond());
        uuidService.setBoardsIdCookie(uuidRequestDto.getUuidFirst(), uuidRequestDto.getUuidSecond(), response, request);
        String cookie = response.getHeader("set-cookie");
        log.info("cookie value = {}", cookie);

        HttpSession session = request.getSession();
        Long boardsId = (Long) session.getAttribute("boardsId");
        log.info("session value = {}", boardsId);

        return new ResponseVo<>(true, HttpStatus.OK.value(), null);
    }

    @GetMapping("/test")
    public ResponseVo<Void> postBoardsIdCookie(@LoginUser LoginUserVo loginUserVo,
                                               HttpServletRequest request,
                                               HttpServletResponse response,
                                               @RequestParam("uuidFirst") String uuidFirst,
                                               @RequestParam("uuidSecond") String uuidSecond
    ) {
        log.info("[postBoardsIdCookie test controller] usersId = {}, uuidFirst = {}, uuidSecond = {}",
                loginUserVo.getUserId(), uuidFirst, uuidSecond);
//        uuidService.setBoardsIdCookie(uuidFirst, uuidSecond, response, request);
//        String cookie = response.getHeader("set-cookie");
//        log.info("cookie value = {}", cookie);
//
//        HttpSession session = request.getSession();
//        Long boardsId = (Long) session.getAttribute("boardsId");
//        log.info("session value = {}", boardsId);
        String test = request.getHeader("Guest-Info");
        log.info("test = {}", test);
        return new ResponseVo<>(true, HttpStatus.OK.value(), null);
    }
}





















