package com.wedding.serviceapi.admin.service;

import com.wedding.serviceapi.admin.domain.CalculateAttendance;
import com.wedding.serviceapi.admin.dto.attendance.AttendanceResponseDto;
import com.wedding.serviceapi.guests.domain.AttendanceType;
import com.wedding.serviceapi.guests.domain.Guests;
import com.wedding.serviceapi.guests.repository.GuestsRepository;
import com.wedding.serviceapi.users.domain.Users;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@Slf4j
class AttendanceServiceTest {

    @InjectMocks
    AttendanceService attendanceService;

    @Mock
    GuestsRepository guestsRepository;

    @Spy
    CalculateAttendance calculateAttendance;

    @Test
    @DisplayName("참석자가 아무도 없는 경우")
    void noAnyGuests() {
        // given
        doReturn(List.of()).when(guestsRepository).findAllByBoardsIdWithUsers(anyLong());
        // when
        AttendanceResponseDto attendanceInfo = attendanceService.getAllAttendanceInfo(anyLong());
        // then
        assertThat(attendanceInfo.getYes().getCount()).isEqualTo(0);
        assertThat(attendanceInfo.getYes().getGuestList().size()).isEqualTo(0);
        assertThat(attendanceInfo.getNo().getCount()).isEqualTo(0);
        assertThat(attendanceInfo.getNo().getGuestList().size()).isEqualTo(0);
        assertThat(attendanceInfo.getUnknown().getCount()).isEqualTo(0);
        assertThat(attendanceInfo.getUnknown().getGuestList().size()).isEqualTo(0);
    }

    @Test
    @DisplayName("참석자가 있는 경우")
    void someGuestsExist() {
        // given
        Users user1 = Users.builder().id(1L).name("user1").build();
        Users user2 = Users.builder().id(1L).name("user2").build();
        Users user3 = Users.builder().id(1L).name("user3").build();
        Users user4 = Users.builder().id(1L).name("user4").build();
        Users user5 = Users.builder().id(1L).name("user5").build();
        Users user6 = Users.builder().id(1L).name("user6").build();
        Users user7 = Users.builder().id(1L).name("user7").build();
        Guests guest1 = Guests.builder().users(user1).attendance(AttendanceType.YES).build();
        Guests guest2 = Guests.builder().users(user2).attendance(AttendanceType.YES).build();
        Guests guest3 = Guests.builder().users(user3).attendance(AttendanceType.NO).build();
        Guests guest4 = Guests.builder().users(user4).attendance(AttendanceType.NO).build();
        Guests guest5 = Guests.builder().users(user5).attendance(AttendanceType.UNKNOWN).build();
        Guests guest6 = Guests.builder().users(user6).attendance(AttendanceType.UNKNOWN).build();
        Guests guest7 = Guests.builder().users(user7).attendance(AttendanceType.UNKNOWN).build();
        List<Guests> guestList = List.of(guest1, guest2, guest3, guest4, guest5, guest6, guest7);
        doReturn(guestList).when(guestsRepository).findAllByBoardsIdWithUsers(anyLong());
        // when
        AttendanceResponseDto attendanceInfo = attendanceService.getAllAttendanceInfo(anyLong());
        // then
        assertThat(attendanceInfo.getYes().getCount()).isEqualTo(2);
        assertThat(attendanceInfo.getYes().getRate()).isEqualTo(29);
        assertThat(attendanceInfo.getYes().getGuestList().size()).isEqualTo(2);
        assertThat(attendanceInfo.getNo().getCount()).isEqualTo(2);
        assertThat(attendanceInfo.getNo().getRate()).isEqualTo(29);
        assertThat(attendanceInfo.getNo().getGuestList().size()).isEqualTo(2);
        assertThat(attendanceInfo.getUnknown().getCount()).isEqualTo(3);
        assertThat(attendanceInfo.getUnknown().getRate()).isEqualTo(43);
        assertThat(attendanceInfo.getUnknown().getGuestList().size()).isEqualTo(3);
    }

    @Test
    @DisplayName("참석여부 변경 테스트 성공")
    void changeAttendanceType() {
        // given
        Users user1 = Users.builder().id(1L).name("user1").build();
        Users user2 = Users.builder().id(2L).name("user2").build();
        Users user3 = Users.builder().id(3L).name("user3").build();
        Users user4 = Users.builder().id(4L).name("user4").build();
        Users user5 = Users.builder().id(5L).name("user5").build();
        Users user6 = Users.builder().id(6L).name("user6").build();
        Users user7 = Users.builder().id(7L).name("user7").build();
        Guests guest1 = Guests.builder().users(user1).attendance(AttendanceType.YES).build();
        Guests guest2 = Guests.builder().users(user2).attendance(AttendanceType.YES).build();
        Guests guest3 = Guests.builder().users(user3).attendance(AttendanceType.NO).build();
        Guests guest4 = Guests.builder().users(user4).attendance(AttendanceType.NO).build();
        Guests guest5 = Guests.builder().users(user5).attendance(AttendanceType.UNKNOWN).build();
        Guests guest6 = Guests.builder().users(user6).attendance(AttendanceType.UNKNOWN).build();
        Guests guest7 = Guests.builder().users(user7).attendance(AttendanceType.UNKNOWN).build();
        List<Guests> guestList = List.of(guest1, guest2, guest3, guest4, guest5, guest6, guest7);
        doReturn(guestList).when(guestsRepository).findAllByBoardsIdWithUsers(1L);
        AttendanceType type = AttendanceType.NO;

        // when
        AttendanceResponseDto attendanceInfo = attendanceService.changeAttendance(guestList.get(6).getUsers().getId(), 1L, type);
        // then
        assertThat(attendanceInfo.getYes().getCount()).isEqualTo(2);
        assertThat(attendanceInfo.getYes().getRate()).isEqualTo(29);
        assertThat(attendanceInfo.getYes().getGuestList().size()).isEqualTo(2);
        assertThat(attendanceInfo.getNo().getCount()).isEqualTo(3);
        assertThat(attendanceInfo.getNo().getRate()).isEqualTo(43);
        assertThat(attendanceInfo.getNo().getGuestList().size()).isEqualTo(3);
        assertThat(attendanceInfo.getUnknown().getCount()).isEqualTo(2);
        assertThat(attendanceInfo.getUnknown().getRate()).isEqualTo(29);
        assertThat(attendanceInfo.getUnknown().getGuestList().size()).isEqualTo(2);
    }


}