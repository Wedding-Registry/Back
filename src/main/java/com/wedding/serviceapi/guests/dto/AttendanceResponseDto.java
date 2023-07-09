package com.wedding.serviceapi.guests.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class AttendanceResponseDto {
    private String attend;

    public static AttendanceResponseDto of(String attend) {
        return new AttendanceResponseDto(attend);
    }
}
