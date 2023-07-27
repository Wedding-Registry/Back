package com.wedding.serviceapi.admin.controller;

import com.wedding.serviceapi.admin.dto.alarm.AlarmListResponseDto;
import com.wedding.serviceapi.admin.service.AlarmService;
import com.wedding.serviceapi.auth.vo.LoginUser;
import com.wedding.serviceapi.auth.vo.LoginUserVo;
import com.wedding.serviceapi.common.vo.ResponseVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/admin/alarm")
@RequiredArgsConstructor
public class AlarmController {

    private final AlarmService alarmService;

    @GetMapping
    public ResponseVo<AlarmListResponseDto> getAllAlarmList(@LoginUser LoginUserVo loginUserVo) {
        log.info("[getAllAlarmList controller] usersId = {}, boardsId = {}", loginUserVo.getUserId(), loginUserVo.getBoardsId());
        AlarmListResponseDto data = alarmService.getAllAlarmList(loginUserVo.getBoardsId());

        return ResponseVo.ok(data);
    }
}
