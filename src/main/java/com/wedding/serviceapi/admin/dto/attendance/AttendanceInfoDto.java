package com.wedding.serviceapi.admin.dto.attendance;

import lombok.Getter;

import java.util.List;

@Getter
public class AttendanceInfoDto {
    private Integer count;
    private long rate;
    private List<AttendanceUserInfoDto> guestList;

    public static AttendanceInfoDto empty() {
        return new AttendanceInfoDto(0, 0, List.of());
    }

    public static AttendanceInfoDto of(Integer count, long rate, List<AttendanceUserInfoDto> guestList) {
        return new AttendanceInfoDto(count, rate, guestList);
    }

    private AttendanceInfoDto(Integer count, long rate, List<AttendanceUserInfoDto> guestList) {
        this.count = count;
        this.rate = rate;
        this.guestList = guestList;
    }
}
