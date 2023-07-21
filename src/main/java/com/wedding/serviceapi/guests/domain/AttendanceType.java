package com.wedding.serviceapi.guests.domain;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum AttendanceType {
    NO("no", "불참"), YES("yes", "참석"), UNKNOWN("unknown", "미정");

    private final String attendance;
    private final String attendanceByKoreaLan;

    AttendanceType(String attendance, String attendanceByKoreaLan) {
        this.attendance = attendance;
        this.attendanceByKoreaLan = attendanceByKoreaLan;
    }

    public static AttendanceType checkAttendance(String guestAnswer) {
        return Arrays.stream(AttendanceType.values())
                .filter(el -> guestAnswer.equalsIgnoreCase(el.attendance))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("잘못된 참석 여부 입니다."));
    }
}
