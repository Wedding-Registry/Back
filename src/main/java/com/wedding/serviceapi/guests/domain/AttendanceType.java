package com.wedding.serviceapi.guests.domain;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum AttendanceType {
    NO("no"), YES("yes"), UNKNOWN("unknown");

    private final String attendance;

    AttendanceType(String attendance) {
        this.attendance = attendance;
    }

    public static AttendanceType checkAttendance(String guestAnswer) {
        return Arrays.stream(AttendanceType.values())
                .filter(el -> guestAnswer.equalsIgnoreCase(el.attendance))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("잘못된 참석 여부 입니다."));
    }
}
