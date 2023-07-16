package com.wedding.serviceapi.admin.dto.attendance;

import lombok.Getter;

@Getter
public class AttendanceResponseDto {
    private AttendanceInfoDto yes;
    private AttendanceInfoDto no;
    private AttendanceInfoDto unknown;

    public static AttendanceResponseDto emptyGuests() {
        AttendanceResponseDto result = new AttendanceResponseDto();
        result.zeroAttendance();
        return result;
    }

    private void zeroAttendance() {
        this.yes = AttendanceInfoDto.empty();
        this.no = AttendanceInfoDto.empty();
        this.unknown = AttendanceInfoDto.empty();
    }

    public static AttendanceResponseDto of(AttendanceInfoDto yes, AttendanceInfoDto no, AttendanceInfoDto unknown) {
        AttendanceResponseDto result = new AttendanceResponseDto();
        result.yes = yes;
        result.no = no;
        result.unknown = unknown;
        return result;
    }
}
