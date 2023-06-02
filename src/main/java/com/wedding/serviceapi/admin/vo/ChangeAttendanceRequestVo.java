package com.wedding.serviceapi.admin.vo;

import com.wedding.serviceapi.guests.domain.AttendanceType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ChangeAttendanceRequestVo {
    private Long userId;
    private String type;
}
