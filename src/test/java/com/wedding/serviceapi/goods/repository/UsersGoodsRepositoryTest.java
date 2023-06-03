package com.wedding.serviceapi.goods.repository;

import com.wedding.serviceapi.boards.domain.Boards;
import com.wedding.serviceapi.boards.repository.BoardsRepository;
import com.wedding.serviceapi.goods.domain.Commerce;
import com.wedding.serviceapi.goods.domain.Goods;
import com.wedding.serviceapi.goods.domain.UsersGoods;
import com.wedding.serviceapi.guests.domain.GoodsDonation;
import com.wedding.serviceapi.guests.domain.Guests;
import com.wedding.serviceapi.guests.repository.GoodsDonationRepository;
import com.wedding.serviceapi.guests.repository.GuestsRepository;
import com.wedding.serviceapi.users.domain.LoginType;
import com.wedding.serviceapi.users.domain.Users;
import com.wedding.serviceapi.users.repository.UsersRepository;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import javax.persistence.EntityManager;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Slf4j
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UsersGoodsRepositoryTest {

    @Autowired
    UsersGoodsRepository usersGoodsRepository;
    @Autowired
    GoodsRepository goodsRepository;
    @Autowired
    UsersRepository usersRepository;
    @Autowired
    BoardsRepository boardsRepository;
    @Autowired
    GuestsRepository guestsRepository;
    @Autowired
    GoodsDonationRepository goodsDonationRepository;
    @Autowired
    EntityManager em;

    public String url;
    public Goods goods;
    public Users users;
    public Users savedUsers;
    public UsersGoods usersGoods;
    public Boards savedBoards;

    @BeforeEach
    void setting() {
        url = "testUrl";
        goods = new Goods("imgUrl", url, "goods1", 100000, Commerce.COUPANG);
        users = Users.builder().email("test").password("password").loginType(LoginType.KAKAO).build();
        Boards boards = Boards.builder().users(users).uuidFirst("first").uuidSecond("second").build();

        savedUsers = usersRepository.save(users);
        savedBoards = boardsRepository.save(boards);
        goodsRepository.save(goods);

        usersGoods = UsersGoods.builder().users(savedUsers).goods(goods).boards(savedBoards).updatedUsersGoodsName(goods.getGoodsName()).updatedUsersGoodsPrice(goods.getGoodsPrice()).wishGoods(false).build();
        usersGoodsRepository.save(usersGoods);

        em.flush();
        em.clear();
    }


    @Test
    @DisplayName("사용자 아이디와 상품 아이디로 상품 찾기")
    void findUsersGoodsWithUsersId() {
        // when
        UsersGoods result = usersGoodsRepository.findByIdAndUsersId(usersGoods.getId(), savedUsers.getId()).get();

        // then
        assertThat(result.getUpdatedUsersGoodsName()).isEqualTo("goods1");
//        assertThat(result.getUsers().getEmail()).isEqualTo("test");
    }
    
    @Test
    @DisplayName("후원 정보까지 한번에 가져오는지 테스트")
    @Rollback(value = false)
    void findUsersGoodsWithGoodsDonation() {
        // given
        // 게스트 유저 추가
        Users guest1 = Users.builder().name("guest1").email("guest1").password("password1").loginType(LoginType.KAKAO).build();
        Users guest2 = Users.builder().name("guest2").email("guest2").password("password2").loginType(LoginType.KAKAO).build();
        usersRepository.saveAllAndFlush(List.of(guest1, guest2));
        // 게스트 테이블 추가
        Guests invitedGuest1 = Guests.builder().users(guest1).boards(savedBoards).build();
        Guests invitedGuest2 = Guests.builder().users(guest2).boards(savedBoards).build();
        guestsRepository.saveAllAndFlush(List.of(invitedGuest1, invitedGuest2));
        // goodsDonation 테이블 추가
        GoodsDonation goodsDonation1 = GoodsDonation.builder().guests(invitedGuest1).usersGoods(usersGoods).goodsDonationAmount(10000).build();
        GoodsDonation goodsDonation2 = GoodsDonation.builder().guests(invitedGuest2).usersGoods(usersGoods).goodsDonationAmount(20000).build();
        goodsDonationRepository.saveAllAndFlush(List.of(goodsDonation1, goodsDonation2));

        // when
        List<UsersGoods> usersGoodsList = usersGoodsRepository.findAllDistinctByUsersIdAndBoardsIdNotWishWithUrlAndDonationId(savedUsers.getId(), savedBoards.getId());

        // then
        assertThat(usersGoodsList.size()).isEqualTo(1);
        assertThat(usersGoodsList.get(0).getDonationList().size()).isEqualTo(2);
    }
    
    @Test
    @DisplayName("후원 정보까지 한번에 가져오는지 테스트")
    @Rollback(value = false)
    void findUsersGoodsWithGoodsDonation() {
        // given
        // 게스트 유저 추가
        Users guest1 = Users.builder().name("guest1").email("guest1").password("password1").loginType(LoginType.KAKAO).build();
        Users guest2 = Users.builder().name("guest2").email("guest2").password("password2").loginType(LoginType.KAKAO).build();
        usersRepository.saveAllAndFlush(List.of(guest1, guest2));
        // 게스트 테이블 추가
        Guests invitedGuest1 = Guests.builder().users(guest1).boards(savedBoards).build();
        Guests invitedGuest2 = Guests.builder().users(guest2).boards(savedBoards).build();
        guestsRepository.saveAllAndFlush(List.of(invitedGuest1, invitedGuest2));
        // goodsDonation 테이블 추가
        GoodsDonation goodsDonation1 = GoodsDonation.builder().guests(invitedGuest1).usersGoods(usersGoods).goodsDonationAmount(10000).build();
        GoodsDonation goodsDonation2 = GoodsDonation.builder().guests(invitedGuest2).usersGoods(usersGoods).goodsDonationAmount(20000).build();
        goodsDonationRepository.saveAllAndFlush(List.of(goodsDonation1, goodsDonation2));

        // when
        List<UsersGoods> usersGoodsList = usersGoodsRepository.findAllDistinctByUsersIdAndBoardsIdNotWishWithUrlAndDonationId(savedUsers.getId(), savedBoards.getId());

        // then
        assertThat(usersGoodsList.size()).isEqualTo(1);
        assertThat(usersGoodsList.get(0).getDonationList().size()).isEqualTo(2);
    }
}















