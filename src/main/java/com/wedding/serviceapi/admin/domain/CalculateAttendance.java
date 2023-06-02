package com.wedding.serviceapi.admin.domain;

import com.wedding.serviceapi.admin.dto.attendance.AttendanceInfoDto;
import com.wedding.serviceapi.admin.dto.attendance.AttendanceResponseDto;
import com.wedding.serviceapi.admin.dto.attendance.AttendanceUserInfoDto;
import com.wedding.serviceapi.admin.dto.summary.AttendanceSummaryDto;
import com.wedding.serviceapi.guests.domain.AttendanceType;
import com.wedding.serviceapi.guests.domain.Guests;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CalculateAttendance {

    public AttendanceSummaryDto makeAttendanceSummary(List<Guests> guestsList) {
        int total = guestsList.size();
        if (total == 0) {
            return AttendanceSummaryDto.emptyGuests();
        }

        int yes = 0;
        int no = 0;
        int unknown = 0;

        for (Guests guest : guestsList) {
            if (guest.getAttendance() == AttendanceType.YES) {
                yes += 1;
            } else if (guest.getAttendance() == AttendanceType.NO) {
                no += 1;
            } else {
                unknown += 1;
            }
        }

        long yesRate = Math.round((yes * 100.0 / total));
        long noRate = Math.round((no * 100.0 / total));
        long unKnownRate = Math.round((unknown * 100.0 / total));
        return AttendanceSummaryDto.of(total, yes, yesRate, no, noRate, unknown, unKnownRate);
    }

    public AttendanceResponseDto makeAttendanceResponse(List<Guests> guestsList) {
        int total = guestsList.size();
        if (total == 0) {
            return AttendanceResponseDto.emptyGuests();
        }

        int yes = 0;
        List<AttendanceUserInfoDto> yesList = new ArrayList<>();
        int no = 0;
        List<AttendanceUserInfoDto> noList = new ArrayList<>();
        int unknown = 0;
        List<AttendanceUserInfoDto> unknownList = new ArrayList<>();

        for (Guests guest : guestsList) {
            if (guest.getAttendance() == AttendanceType.YES) {
                yes += 1;
                yesList.add(AttendanceUserInfoDto.from(guest.getUsers()));
            } else if (guest.getAttendance() == AttendanceType.NO) {
                no += 1;
                noList.add(AttendanceUserInfoDto.from(guest.getUsers()));
            } else {
                unknown += 1;
                unknownList.add(AttendanceUserInfoDto.from(guest.getUsers()));
            }
        }

        long yesRate = Math.round((yes * 100.0 / total));
        long noRate = Math.round((no * 100.0 / total));
        long unKnownRate = Math.round((unknown * 100.0 / total));

        AttendanceInfoDto yesInfoDto = AttendanceInfoDto.of(yes, yesRate, yesList);
        AttendanceInfoDto noInfoDto = AttendanceInfoDto.of(no, noRate, noList);
        AttendanceInfoDto unknownInfoDto = AttendanceInfoDto.of(unknown, unKnownRate, unknownList);

        return AttendanceResponseDto.of(yesInfoDto, noInfoDto, unknownInfoDto);
    }
}






















