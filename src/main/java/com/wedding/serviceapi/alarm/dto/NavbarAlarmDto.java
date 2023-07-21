package com.wedding.serviceapi.alarm.dto;

import com.wedding.serviceapi.admin.dto.alarm.DonationUserInfoDto;
import com.wedding.serviceapi.admin.dto.attendance.AttendanceUserInfoDto;
import com.wedding.serviceapi.guests.domain.AttendanceType;
import lombok.*;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@ToString
public class NavbarAlarmDto {

    private String name;
    private String type;
    private String date;
    private AttendanceType attend;
    private String goods;
    private Integer donation;


    public static NavbarAlarmDto from(AttendanceUserInfoDto attendanceUserInfoDto) {
        return NavbarAlarmDto.builder()
                .name(attendanceUserInfoDto.getName())
                .type("attend")
                .date(attendanceUserInfoDto.getDate())
                .attend(attendanceUserInfoDto.getAttendanceType())
                .goods(null)
                .donation(null)
                .build();
    }

    public static NavbarAlarmDto from(DonationUserInfoDto donationUserInfoDto) {
        return NavbarAlarmDto.builder()
                .name(donationUserInfoDto.getName())
                .type("donation")
                .date(donationUserInfoDto.getDate())
                .attend(null)
                .goods(donationUserInfoDto.getGoods())
                .donation(donationUserInfoDto.getAmount())
                .build();
    }
}
