package com.wedding.serviceapi.admin.controller;

import com.wedding.serviceapi.admin.dto.attendance.AttendanceResponseDto;
import com.wedding.serviceapi.admin.dto.attendance.ChangeAttendanceDto;
import com.wedding.serviceapi.admin.service.AttendanceService;
import com.wedding.serviceapi.admin.vo.ChangeAttendanceRequestVo;
import com.wedding.serviceapi.auth.vo.LoginUser;
import com.wedding.serviceapi.auth.vo.LoginUserVo;
import com.wedding.serviceapi.common.vo.ResponseVo;
import com.wedding.serviceapi.guests.domain.AttendanceType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

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
        return ResponseVo.ok(data);
    }

    @PutMapping
    public ResponseVo<AttendanceResponseDto> changeAttendance(@LoginUser LoginUserVo loginUserVo,
                                                              @RequestBody List<ChangeAttendanceRequestVo> requestBody) {
        log.info("[changeAttendance controller] usersId = {}, boardsId = {}", loginUserVo.getUserId(), loginUserVo.getBoardsId());

        List<ChangeAttendanceDto> changeAttendanceDtoList = requestBody.stream()
                .map(changeAttendanceRequestVo -> ChangeAttendanceDto.of(
                        changeAttendanceRequestVo.getUserId(),
                        AttendanceType.checkAttendance(changeAttendanceRequestVo.getType())))
                .collect(Collectors.toList());

        AttendanceResponseDto data = attendanceService.changeAttendance(changeAttendanceDtoList, loginUserVo.getBoardsId());
        return ResponseVo.accepted(data);
    }
}
