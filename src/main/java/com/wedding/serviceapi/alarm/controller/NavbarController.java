package com.wedding.serviceapi.alarm.controller;

import com.wedding.serviceapi.alarm.dto.NavbarAlarmDto;
import com.wedding.serviceapi.alarm.service.NavbarService;
import com.wedding.serviceapi.auth.vo.LoginUser;
import com.wedding.serviceapi.auth.vo.LoginUserVo;
import com.wedding.serviceapi.common.vo.ResponseVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/navbar/alarm/all")
public class NavbarController {

    private final NavbarService navbarService;

    @GetMapping
    public ResponseVo<List<NavbarAlarmDto>> getNavbarAlarm(@LoginUser LoginUserVo loginUserVo) {
        log.info("[getNavbarAlarm controller] usersId = {}, boardsId = {}", loginUserVo.getUserId(), loginUserVo.getBoardsId());
        List<NavbarAlarmDto> data = navbarService.getNavbarAlarm(loginUserVo.getBoardsId());

        return ResponseVo.ok(data);
    }



}
