package com.wedding.serviceapi.alarm.service;

import com.wedding.serviceapi.alarm.dto.NavbarAlarmDto;
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
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class NavbarServiceTest {

    @Autowired
    private NavbarService navbarService;
    @Autowired
    private UsersRepository usersRepository;
    @Autowired
    private BoardsRepository boardsRepository;
    @Autowired
    private GoodsRepository goodsRepository;
    @Autowired
    private UsersGoodsRepository usersGoodsRepository;
    @Autowired
    private GuestsRepository guestsRepository;
    @Autowired
    private GoodsDonationRepository goodsDonationRepository;
    @Autowired
    private EntityManager em;

    @Test
    @DisplayName("네비게이션 바에서 알람 리스트 목록을 가져옵니다.")
    void getNavbarAlarm() throws InterruptedException {
        // given
        Users user1 = Users.builder().name("user1").build();
        Users guest1 = Users.builder().name("guest1").build();
        Users guest2 = Users.builder().name("guest2").build();
        usersRepository.saveAllAndFlush(List.of(user1, guest1, guest2));
        Boards boards = Boards.builder().users(user1).uuidFirst("first").uuidSecond("second").build();
        Boards savedBoards = boardsRepository.saveAndFlush(boards);
        Goods goods1 = Goods.builder().goodsName("goods1").goodsPrice(10000).goodsUrl("url1").build();
        Goods goods2 = Goods.builder().goodsName("goods2").goodsPrice(20000).goodsUrl("url2").build();
        goodsRepository.saveAllAndFlush(List.of(goods1, goods2));
        UsersGoods usersGoods1 = UsersGoods.builder().users(user1).boards(boards).goods(goods1).wishGoods(false).updatedUsersGoodsName("goods1").updatedUsersGoodsPrice(10000).build();
        UsersGoods usersGoods2 = UsersGoods.builder().users(user1).boards(boards).goods(goods2).wishGoods(false).updatedUsersGoodsName("goods2").updatedUsersGoodsPrice(20000).build();
        usersGoodsRepository.saveAllAndFlush(List.of(usersGoods1, usersGoods2));
        Guests guests1 = Guests.builder().boards(boards).users(guest1).attendance(AttendanceType.YES).build();
        Guests guests2 = Guests.builder().boards(boards).users(guest2).attendance(AttendanceType.NO).build();
        GoodsDonation goodsDonation1 = GoodsDonation.builder().usersGoods(usersGoods1).guests(guests1).goodsDonationAmount(1000).build();
        GoodsDonation goodsDonation2 = GoodsDonation.builder().usersGoods(usersGoods1).guests(guests2).goodsDonationAmount(2000).build();
        GoodsDonation goodsDonation3 = GoodsDonation.builder().usersGoods(usersGoods2).guests(guests1).goodsDonationAmount(3000).build();

        guestsRepository.save(guests1);
        goodsDonationRepository.save(goodsDonation1);
        guestsRepository.save(guests2);
        goodsDonationRepository.save(goodsDonation2);
        goodsDonationRepository.save(goodsDonation3);

        // when
        List<NavbarAlarmDto> data = navbarService.getNavbarAlarm(savedBoards.getId());
        // then
        assertThat(data).hasSize(5);
//                .extracting("name", "type", "attend", "goods", "donation")
//                .containsExactly(
//                        tuple("guest1", "donation", null, "goods2", 3000),
//                        tuple("guest2", "donation", null, "goods1", 2000),
//                        tuple("guest2", "attend", AttendanceType.NO, null, null),
//                        tuple("guest1", "donation", null, "goods1", 1000),
//                        tuple("guest1", "attend", AttendanceType.YES, null, null)
//                );

    }
}