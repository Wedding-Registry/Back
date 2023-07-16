package com.wedding.serviceapi.admin.dto.alarm;

import com.wedding.serviceapi.admin.dto.attendance.AttendanceUserInfoDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
public class AlarmListResponseDto {

    private List<AttendanceUserInfoDto> attendance;
    private List<DonationUserInfoDto> donation;

    private AlarmListResponseDto(List<AttendanceUserInfoDto> attendance, List<DonationUserInfoDto> donation) {
        this.attendance = attendance;
        this.donation = donation;
    }

    public static AlarmListResponseDto from(List<AttendanceUserInfoDto> attendance, List<DonationUserInfoDto> donation) {
        return new AlarmListResponseDto(attendance, donation);
    }
}
