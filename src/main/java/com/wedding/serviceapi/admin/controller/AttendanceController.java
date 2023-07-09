package com.wedding.serviceapi.admin.controller;

import com.wedding.serviceapi.admin.dto.attendance.AttendanceResponseDto;
import com.wedding.serviceapi.admin.service.AttendanceService;
import com.wedding.serviceapi.admin.vo.ChangeAttendanceRequestVo;
import com.wedding.serviceapi.auth.vo.LoginUser;
import com.wedding.serviceapi.auth.vo.LoginUserVo;
import com.wedding.serviceapi.common.vo.ResponseVo;
import com.wedding.serviceapi.guests.domain.AttendanceType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/attendance")
@Slf4j
@RequiredArgsConstructor
public class AttendanceController {

    private final AttendanceService attendanceService;

    @GetMapping("/detail")
    public ResponseVo<AttendanceResponseDto> getAllAttendanceInfo(@LoginUser LoginUserVo loginUserVo) {
        log.info("[getAllAttendanceInfo controller] usersId = {}, boardsId = {}", loginUserVo.getUserId(), loginUserVo.getBoardsId());
        AttendanceResponseDto data = attendanceService.getAllAttendanceInfo(loginUserVo.getBoardsId());
        return new ResponseVo<>(true, HttpStatus.OK.value(), data);
    }

    @PutMapping
    public ResponseVo<AttendanceResponseDto> changeAttendance(@LoginUser LoginUserVo loginUserVo,
                                             @RequestBody ChangeAttendanceRequestVo changeAttendanceRequestVo) {
        log.info("[changeAttendance controller] usersId = {}, boardsId = {}", loginUserVo.getUserId(), loginUserVo.getBoardsId());

        AttendanceType attendanceType = AttendanceType.checkAttendance(changeAttendanceRequestVo.getType());
        AttendanceResponseDto data = attendanceService.changeAttendance(changeAttendanceRequestVo.getUserId(), loginUserVo.getBoardsId(), attendanceType);
        return new ResponseVo<>(true, HttpStatus.ACCEPTED.value(), data);
    }
}
