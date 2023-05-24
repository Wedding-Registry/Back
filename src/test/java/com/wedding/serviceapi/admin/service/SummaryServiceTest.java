package com.wedding.serviceapi.admin.service;

import com.wedding.serviceapi.admin.domain.CalculateAttendance;
import com.wedding.serviceapi.admin.dto.summary.AttendanceSummaryDto;
import com.wedding.serviceapi.admin.dto.summary.DonationSummaryDto;
import com.wedding.serviceapi.goods.domain.UsersGoods;
import com.wedding.serviceapi.goods.repository.UsersGoodsRepository;
import com.wedding.serviceapi.guests.domain.AttendanceType;
import com.wedding.serviceapi.guests.domain.Guests;
import com.wedding.serviceapi.guests.repository.GuestsRepository;
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

@ExtendWith(MockitoExtension.class)
@Slf4j
class SummaryServiceTest {

    @InjectMocks
    SummaryService summaryService;

    @Mock
    GuestsRepository guestsRepository;
    @Mock
    UsersGoodsRepository usersGoodsRepository;

    @Spy
    CalculateAttendance calculateAttendance;

    @Test
    @DisplayName("참석자가 아무도 없는 경우")
    void noAnyGuests() {
        // given
        Mockito.doReturn(List.of()).when(guestsRepository).findAllByBoardsId(Mockito.anyLong());
        // when
        AttendanceSummaryDto attendanceSummary = summaryService.getAttendanceSummary(Mockito.anyLong());
        // then
        Assertions.assertThat(attendanceSummary.getYesRate()).isZero();
    }

    @Test
    @DisplayName("참석자가 있는 경우")
    void someGuestsExist() {
        // given
        Guests guest1 = Guests.builder().attendance(AttendanceType.YES).build();
        Guests guest2 = Guests.builder().attendance(AttendanceType.YES).build();
        Guests guest3 = Guests.builder().attendance(AttendanceType.NO).build();
        Guests guest4 = Guests.builder().attendance(AttendanceType.NO).build();
        Guests guest5 = Guests.builder().attendance(AttendanceType.UNKNOWN).build();
        Guests guest6 = Guests.builder().attendance(AttendanceType.UNKNOWN).build();
        Guests guest7 = Guests.builder().attendance(AttendanceType.UNKNOWN).build();
        List<Guests> guestsList = List.of(guest1, guest2, guest3, guest4, guest5, guest6, guest7);
        Mockito.doReturn(guestsList).when(guestsRepository).findAllByBoardsId(Mockito.anyLong());
        // when
        AttendanceSummaryDto attendanceSummary = summaryService.getAttendanceSummary(Mockito.anyLong());
        // then
        Assertions.assertThat(attendanceSummary.getTotal()).isEqualTo(7);
        Assertions.assertThat(attendanceSummary.getYes()).isEqualTo(2);
        Assertions.assertThat(attendanceSummary.getYesRate()).isEqualTo(29);
        Assertions.assertThat(attendanceSummary.getNo()).isEqualTo(2);
        Assertions.assertThat(attendanceSummary.getNoRate()).isEqualTo(29);
        Assertions.assertThat(attendanceSummary.getUnknown()).isEqualTo(3);
        Assertions.assertThat(attendanceSummary.getUnknownRate()).isEqualTo(43);
    }

    @Test
    @DisplayName("내림차순 정렬 테스트")
    void sortUsersGoods() {
        // given
        UsersGoods goods1 = UsersGoods.builder().id(1L).updatedUsersGoodsName("goods1").updatedUsersGoodsPrice(100).usersGoodsTotalDonation(90).build();
        UsersGoods goods2 = UsersGoods.builder().id(1L).updatedUsersGoodsName("goods1").updatedUsersGoodsPrice(100).usersGoodsTotalDonation(80).build();
        UsersGoods goods3 = UsersGoods.builder().id(1L).updatedUsersGoodsName("goods1").updatedUsersGoodsPrice(100).usersGoodsTotalDonation(95).build();
        UsersGoods goods4 = UsersGoods.builder().id(1L).updatedUsersGoodsName("goods1").updatedUsersGoodsPrice(100).usersGoodsTotalDonation(79).build();
        UsersGoods goods5 = UsersGoods.builder().id(1L).updatedUsersGoodsName("goods1").updatedUsersGoodsPrice(100).usersGoodsTotalDonation(30).build();
        List<UsersGoods> goodsList = List.of(goods1, goods2, goods3, goods4, goods5);
        Mockito.doReturn(goodsList).when(usersGoodsRepository).findAllByUsersIdAndBoardsIdNotWish(1L, 1L);
        // when
        List<DonationSummaryDto> donationSummary = summaryService.getDonationSummary(1L, 1L);
        // then
        Assertions.assertThat(donationSummary.size()).isEqualTo(3);
        Assertions.assertThat(donationSummary.get(0).getUsersGoodsTotalDonation()).isEqualTo(95);
    }
}































