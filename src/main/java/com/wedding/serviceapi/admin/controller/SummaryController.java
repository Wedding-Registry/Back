package com.wedding.serviceapi.admin.controller;

import com.wedding.serviceapi.admin.dto.summary.AttendanceSummaryDto;
import com.wedding.serviceapi.admin.dto.summary.DonationSummaryDto;
import com.wedding.serviceapi.admin.service.SummaryService;
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

@Slf4j
@RequestMapping("/admin/summary")
@RequiredArgsConstructor
@RestController
public class SummaryController {

    private final SummaryService summaryService;

    @GetMapping("/attendance")
    public ResponseVo<AttendanceSummaryDto> findAttendanceRate(@LoginUser LoginUserVo loginUserVo) {

        AttendanceSummaryDto data = summaryService.getAttendanceSummary(loginUserVo.getBoardsId());
        return ResponseVo.ok(data);
    }

    @GetMapping("/donation")
    public ResponseVo<List<DonationSummaryDto>> findAllDonation(@LoginUser LoginUserVo loginUserVo) {

        List<DonationSummaryDto> data = summaryService.getDonationSummary(loginUserVo.getUserId(), loginUserVo.getBoardsId());
        return ResponseVo.ok(data);
    }
}
