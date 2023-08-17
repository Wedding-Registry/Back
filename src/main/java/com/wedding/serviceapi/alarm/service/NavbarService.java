package com.wedding.serviceapi.alarm.service;

import com.wedding.serviceapi.admin.dto.alarm.AlarmListResponseDto;
import com.wedding.serviceapi.admin.dto.alarm.DonationUserInfoDto;
import com.wedding.serviceapi.admin.dto.attendance.AttendanceUserInfoDto;
import com.wedding.serviceapi.admin.service.AlarmService;
import com.wedding.serviceapi.alarm.dto.NavbarAlarmDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class NavbarService {

    private final AlarmService alarmService;

    public List<NavbarAlarmDto> getNavbarAlarm(Long boardsId) {
        AlarmListResponseDto alarmList = alarmService.getLimitedAlarmList(boardsId, 5);

        List<AttendanceUserInfoDto> attendanceList = alarmList.getAttendance();
        List<DonationUserInfoDto> donationList = alarmList.getDonation();

        // TODO: 2023/06/29 이후에 로직 변경 필요 -> 현재는 그냥 다 넣어서 다시 정렬하지만 조금 더 좋은 방법으로 리팩터링 예정
        List<NavbarAlarmDto> attendanceNavbarList = attendanceList.stream().map(NavbarAlarmDto::from).collect(Collectors.toList());
        List<NavbarAlarmDto> donationNavbarList = donationList.stream().map(NavbarAlarmDto::from).collect(Collectors.toList());

        List<NavbarAlarmDto> navbarAlarmList = new ArrayList<>();
        navbarAlarmList.addAll(attendanceNavbarList);
        navbarAlarmList.addAll(donationNavbarList);

        List<NavbarAlarmDto> sortedList = navbarAlarmList.stream()
                .sorted((navbarAlarm1, navbarAlarm2) -> navbarAlarm2.getDate().compareTo(navbarAlarm1.getDate()))
                .collect(Collectors.toList());

        return sortedList.size() <= 5 ? sortedList : sortedList.subList(0, 5);
    }

}
