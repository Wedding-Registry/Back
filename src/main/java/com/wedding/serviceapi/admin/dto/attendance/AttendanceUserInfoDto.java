package com.wedding.serviceapi.admin.dto.attendance;

import com.wedding.serviceapi.users.domain.Users;
import lombok.Getter;

@Getter
public class AttendanceUserInfoDto {
    private Long userId;
    private String name;

    public static AttendanceUserInfoDto from(Users users) {
        return new AttendanceUserInfoDto(users.getId(), users.getName());
    }

    private AttendanceUserInfoDto(Long userId, String name) {
        this.userId = userId;
        this.name = name;
    }
}
