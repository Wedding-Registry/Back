package com.wedding.serviceapi.admin.dto.attendance;

import com.wedding.serviceapi.guests.domain.AttendanceType;
import com.wedding.serviceapi.guests.domain.Guests;
import com.wedding.serviceapi.users.domain.Users;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.format.DateTimeFormatter;

@Getter
@AllArgsConstructor
public class AttendanceUserInfoDto {
    private Long userId;
    private String name;
  
    private String date;
    private String time;
    private AttendanceType attend;

    private AttendanceUserInfoDto(Long userId, String name) {
        this.userId = userId;
        this.name = name;
    }

    public static AttendanceUserInfoDto from(Users users) {
        return new AttendanceUserInfoDto(users.getId(), users.getName());
    }

    public static AttendanceUserInfoDto from(Guests guests) {
        Users users = guests.getUsers();
        String date = guests.getUpdatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String time = guests.getUpdatedAt().format(DateTimeFormatter.ofPattern("hh-mm"));
        return new AttendanceUserInfoDto(users.getId(), users.getName(), date, time, guests.getAttendance());
    }
}
