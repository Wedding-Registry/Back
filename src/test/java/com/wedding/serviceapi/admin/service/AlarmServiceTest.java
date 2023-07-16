package com.wedding.serviceapi.admin.service;

import com.wedding.serviceapi.admin.dto.alarm.AlarmListResponseDto;
import com.wedding.serviceapi.boards.domain.Boards;
import com.wedding.serviceapi.boards.repository.BoardsRepository;
import com.wedding.serviceapi.goods.domain.Goods;
import com.wedding.serviceapi.goods.domain.UsersGoods;
import com.wedding.serviceapi.goods.repository.GoodsRepository;
import com.wedding.serviceapi.goods.repository.UsersGoodsRepository;
import com.wedding.serviceapi.guests.domain.AttendanceType;
import com.wedding.serviceapi.guests.domain.GoodsDonation;
import com.wedding.serviceapi.guests.domain.Guests;
import com.wedding.serviceapi.guests.repository.GoodsDonationRepository;
import com.wedding.serviceapi.guests.repository.GuestsRepository;
import com.wedding.serviceapi.users.domain.Users;
import com.wedding.serviceapi.users.repository.UsersRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AlarmServiceTest {

    private final GoodsDonationRepository goodsDonationRepository;
    private final UsersGoodsRepository usersGoodsRepository;
    private final UsersRepository usersRepository;
    private final BoardsRepository boardsRepository;
    private final GuestsRepository guestsRepository;
    private final GoodsRepository goodsRepository;
    private final AlarmService alarmService;

    @Autowired
    public AlarmServiceTest(GoodsDonationRepository goodsDonationRepository, UsersGoodsRepository usersGoodsRepository, UsersRepository usersRepository, BoardsRepository boardsRepository, GuestsRepository guestsRepository, GoodsRepository goodsRepository, AlarmService alarmService) {
        this.goodsDonationRepository = goodsDonationRepository;
        this.usersGoodsRepository = usersGoodsRepository;
        this.usersRepository = usersRepository;
        this.boardsRepository = boardsRepository;
        this.guestsRepository = guestsRepository;
        this.goodsRepository = goodsRepository;
        this.alarmService = alarmService;
    }

    private Boards savedBoards;

    @BeforeEach
    void init() throws InterruptedException {
        Users user1 = Users.builder().name("user1").build();
        Users guest1 = Users.builder().name("guest1").build();
        Users guest2 = Users.builder().name("guest1").build();
        usersRepository.saveAllAndFlush(List.of(user1, guest1, guest2));
        Boards boards = Boards.builder().users(user1).uuidFirst("first").uuidSecond("second").build();
        savedBoards = boardsRepository.saveAndFlush(boards);
        Goods goods1 = Goods.builder().goodsName("goods1").goodsPrice(10000).goodsUrl("url1").build();
        Goods goods2 = Goods.builder().goodsName("goods2").goodsPrice(20000).goodsUrl("url2").build();
        goodsRepository.saveAllAndFlush(List.of(goods1, goods2));
        UsersGoods usersGoods1 = UsersGoods.builder().users(user1).boards(boards).goods(goods1).wishGoods(false).updatedUsersGoodsName("goods1").updatedUsersGoodsPrice(10000).build();
        UsersGoods usersGoods2 = UsersGoods.builder().users(user1).boards(boards).goods(goods2).wishGoods(false).updatedUsersGoodsName("goods2").updatedUsersGoodsPrice(20000).build();
        usersGoodsRepository.saveAllAndFlush(List.of(usersGoods1, usersGoods2));
        Guests guests1 = Guests.builder().boards(boards).users(guest1).attendance(AttendanceType.YES).build();
        Guests guests2 = Guests.builder().boards(boards).users(guest2).attendance(AttendanceType.NO).build();
        guestsRepository.saveAllAndFlush(List.of(guests1, guests2));
        GoodsDonation goodsDonation1 = GoodsDonation.builder().usersGoods(usersGoods1).guests(guests1).goodsDonationAmount(1000).build();
        Thread.sleep(1000);
        GoodsDonation goodsDonation2 = GoodsDonation.builder().usersGoods(usersGoods1).guests(guests2).goodsDonationAmount(2000).build();
        Thread.sleep(1000);
        GoodsDonation goodsDonation3 = GoodsDonation.builder().usersGoods(usersGoods2).guests(guests1).goodsDonationAmount(3000).build();
        goodsDonationRepository.saveAllAndFlush(List.of(goodsDonation1, goodsDonation2, goodsDonation3));
    }

    @Test
    @DisplayName("모든 알람 목록을 가져오는 테스트")
    void getAllAlarm() {
        // when
        AlarmListResponseDto responseDto = alarmService.getAllAlarmList(savedBoards.getId());
        // then
        assertThat(responseDto.getAttendance().size()).isEqualTo(2);
        assertThat(responseDto.getDonation().size()).isEqualTo(3);
    }

}





















