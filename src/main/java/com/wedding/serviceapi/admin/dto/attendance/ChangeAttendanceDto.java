package com.wedding.serviceapi.admin.dto.attendance;

import com.wedding.serviceapi.guests.domain.AttendanceType;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ChangeAttendanceDto {
    private Long userId;
    private AttendanceType attendanceType;

    @Builder
    private ChangeAttendanceDto(Long userId, AttendanceType attendanceType) {
        this.userId = userId;
        this.attendanceType = attendanceType;
    }

    public static ChangeAttendanceDto of(Long userId, AttendanceType attendanceType) {
        return ChangeAttendanceDto.builder()
                .userId(userId)
                .attendanceType(attendanceType)
                .build();
    }
}
