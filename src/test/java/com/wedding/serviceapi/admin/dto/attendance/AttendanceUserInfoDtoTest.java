package com.wedding.serviceapi.admin.dto.attendance;

import com.wedding.serviceapi.guests.domain.AttendanceType;
import com.wedding.serviceapi.guests.domain.Guests;
import com.wedding.serviceapi.users.domain.Users;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class AttendanceUserInfoDtoTest {

    @DisplayName("정적 메서드로 dto 생성 시 날짜랑 시간, 참석 여부 확인")
    @Test
    void staticFromTest() {
        // given
        Users user = Users.builder()
                .id(1L)
                .name("user")
                .build();
        Guests guests = Guests.builder()
                .users(user)
                .attendance(AttendanceType.UNKNOWN)
                .build();
        guests.setUpdatedAt(LocalDateTime.of(2023, 7, 21, 23, 14, 15));
        // when
        AttendanceUserInfoDto result = AttendanceUserInfoDto.from(guests);
        // then
        assertThat(result.getAttend()).isEqualTo("미정");
        assertThat(result.getDate()).isEqualTo("2023-07-21");
        assertThat(result.getTime()).isEqualTo("23:14");
    }
}