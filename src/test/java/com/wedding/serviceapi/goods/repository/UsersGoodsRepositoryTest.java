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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
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
    @DisplayName("usersId와 boardsId를 통해 usersGoods 목록 가져오기")
    void findByUsersIdAndBoardsId() {
        // when
        List<UsersGoods> result = usersGoodsRepository.findAllByUsersIdAndBoardsIdNotWish(savedUsers.getId(), savedBoards.getId());
        // then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getUpdatedUsersGoodsName()).isEqualTo("goods1");
    }

    @Test
    @DisplayName("boardsId를 통해 usersGoods 목록 가져오기")
    void findByBoardsId() {
        // when
        List<UsersGoods> result = usersGoodsRepository.findAllByBoardsIdNotWish(savedBoards.getId());
        // then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getUpdatedUsersGoodsName()).isEqualTo("goods1");
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
    @DisplayName("원하는 아이템으로 등록한 상품을 lastId 변수 없이 처음부터 제대로 가져오는지 테스트")
    void sliceQueryTestWithNoLastId() {
        // given
        Goods goods1 = new Goods("imgUrl1", url, "goods1", 100000, Commerce.COUPANG);
        Goods goods2 = new Goods("imgUrl2", url, "goods2", 200000, Commerce.COUPANG);
        Goods goods3 = new Goods("imgUrl3", url, "goods3", 300000, Commerce.COUPANG);
        Goods goods4 = new Goods("imgUrl4", url, "goods4", 400000, Commerce.COUPANG);
        Goods goods5 = new Goods("imgUrl5", url, "goods5", 500000, Commerce.COUPANG);
        Goods goods6 = new Goods("imgUrl6", url, "goods6", 600000, Commerce.COUPANG);
        Goods goods7 = new Goods("imgUrl7", url, "goods7", 700000, Commerce.COUPANG);
        Goods goods8 = new Goods("imgUrl8", url, "goods8", 800000, Commerce.COUPANG);
        Goods goods9 = new Goods("imgUrl9", url, "goods9", 900000, Commerce.COUPANG);
        Goods goods10 = new Goods("imgUrl10", url, "goods10", 110000, Commerce.COUPANG);
        Goods goods11 = new Goods("imgUrl11", url, "goods11", 120000, Commerce.COUPANG);
        Goods goods12 = new Goods("imgUrl12", url, "goods12", 130000, Commerce.COUPANG);
        Goods goods13 = new Goods("imgUrl13", url, "goods13", 140000, Commerce.COUPANG);
        Goods goods14 = new Goods("imgUrl14", url, "goods14", 150000, Commerce.COUPANG);
        goodsRepository.saveAllAndFlush(List.of(goods1, goods2, goods3, goods4, goods5, goods6, goods7, goods8, goods9, goods10, goods11, goods12, goods13, goods14));
        UsersGoods usersGoods1 = UsersGoods.builder().users(savedUsers).goods(goods1).boards(savedBoards).updatedUsersGoodsName(goods1.getGoodsName()).updatedUsersGoodsPrice(goods1.getGoodsPrice()).wishGoods(true).build();
        UsersGoods usersGoods2 = UsersGoods.builder().users(savedUsers).goods(goods1).boards(savedBoards).updatedUsersGoodsName(goods2.getGoodsName()).updatedUsersGoodsPrice(goods2.getGoodsPrice()).wishGoods(true).build();
        UsersGoods usersGoods3 = UsersGoods.builder().users(savedUsers).goods(goods1).boards(savedBoards).updatedUsersGoodsName(goods3.getGoodsName()).updatedUsersGoodsPrice(goods3.getGoodsPrice()).wishGoods(true).build();
        UsersGoods usersGoods4 = UsersGoods.builder().users(savedUsers).goods(goods1).boards(savedBoards).updatedUsersGoodsName(goods4.getGoodsName()).updatedUsersGoodsPrice(goods4.getGoodsPrice()).wishGoods(true).build();
        UsersGoods usersGoods5 = UsersGoods.builder().users(savedUsers).goods(goods1).boards(savedBoards).updatedUsersGoodsName(goods5.getGoodsName()).updatedUsersGoodsPrice(goods5.getGoodsPrice()).wishGoods(true).build();
        UsersGoods usersGoods6 = UsersGoods.builder().users(savedUsers).goods(goods1).boards(savedBoards).updatedUsersGoodsName(goods6.getGoodsName()).updatedUsersGoodsPrice(goods6.getGoodsPrice()).wishGoods(true).build();
        UsersGoods usersGoods7 = UsersGoods.builder().users(savedUsers).goods(goods1).boards(savedBoards).updatedUsersGoodsName(goods7.getGoodsName()).updatedUsersGoodsPrice(goods7.getGoodsPrice()).wishGoods(true).build();
        UsersGoods usersGoods8 = UsersGoods.builder().users(savedUsers).goods(goods1).boards(savedBoards).updatedUsersGoodsName(goods8.getGoodsName()).updatedUsersGoodsPrice(goods8.getGoodsPrice()).wishGoods(true).build();
        UsersGoods usersGoods9 = UsersGoods.builder().users(savedUsers).goods(goods1).boards(savedBoards).updatedUsersGoodsName(goods9.getGoodsName()).updatedUsersGoodsPrice(goods9.getGoodsPrice()).wishGoods(true).build();
        UsersGoods usersGoods10 = UsersGoods.builder().users(savedUsers).goods(goods1).boards(savedBoards).updatedUsersGoodsName(goods10.getGoodsName()).updatedUsersGoodsPrice(goods10.getGoodsPrice()).wishGoods(true).build();
        UsersGoods usersGoods11 = UsersGoods.builder().users(savedUsers).goods(goods1).boards(savedBoards).updatedUsersGoodsName(goods11.getGoodsName()).updatedUsersGoodsPrice(goods11.getGoodsPrice()).wishGoods(true).build();
        UsersGoods usersGoods12 = UsersGoods.builder().users(savedUsers).goods(goods1).boards(savedBoards).updatedUsersGoodsName(goods12.getGoodsName()).updatedUsersGoodsPrice(goods12.getGoodsPrice()).wishGoods(true).build();
        UsersGoods usersGoods13 = UsersGoods.builder().users(savedUsers).goods(goods1).boards(savedBoards).updatedUsersGoodsName(goods13.getGoodsName()).updatedUsersGoodsPrice(goods13.getGoodsPrice()).wishGoods(true).build();
        UsersGoods usersGoods14 = UsersGoods.builder().users(savedUsers).goods(goods1).boards(savedBoards).updatedUsersGoodsName(goods14.getGoodsName()).updatedUsersGoodsPrice(goods14.getGoodsPrice()).wishGoods(true).build();
        usersGoodsRepository.saveAllAndFlush(List.of(usersGoods1, usersGoods2, usersGoods3, usersGoods4, usersGoods5, usersGoods6, usersGoods7, usersGoods8, usersGoods9, usersGoods10, usersGoods11, usersGoods12, usersGoods13, usersGoods14));
        // when
        Slice<UsersGoods> usersGoodsSlice = usersGoodsRepository.findByUsersIdAndBoardsIdAndWishGoods(savedUsers.getId(), savedBoards.getId(), true, PageRequest.of(0, 4, Sort.by("id").descending()));
        // then
        assertThat(usersGoodsSlice.getSize()).isEqualTo(4);
        assertThat(usersGoodsSlice).extracting("updatedUsersGoodsName", "updatedUsersGoodsPrice")
                .containsExactly(
                        tuple("goods14", 150000),
                        tuple("goods13", 140000),
                        tuple("goods12", 130000),
                        tuple("goods11", 120000)
                );
    }

    @Test
    @DisplayName("원하는 아이템으로 등록한 상품을 lastId 제대로 가져오는지 테스트")
    void sliceQueryTestWitLastId() {
        // given
        Goods goods1 = new Goods("imgUrl1", url, "goods1", 100000, Commerce.COUPANG);
        Goods goods2 = new Goods("imgUrl2", url, "goods2", 200000, Commerce.COUPANG);
        Goods goods3 = new Goods("imgUrl3", url, "goods3", 300000, Commerce.COUPANG);
        Goods goods4 = new Goods("imgUrl4", url, "goods4", 400000, Commerce.COUPANG);
        Goods goods5 = new Goods("imgUrl5", url, "goods5", 500000, Commerce.COUPANG);
        Goods goods6 = new Goods("imgUrl6", url, "goods6", 600000, Commerce.COUPANG);
        Goods goods7 = new Goods("imgUrl7", url, "goods7", 700000, Commerce.COUPANG);
        Goods goods8 = new Goods("imgUrl8", url, "goods8", 800000, Commerce.COUPANG);
        Goods goods9 = new Goods("imgUrl9", url, "goods9", 900000, Commerce.COUPANG);
        Goods goods10 = new Goods("imgUrl10", url, "goods10", 110000, Commerce.COUPANG);
        Goods goods11 = new Goods("imgUrl11", url, "goods11", 120000, Commerce.COUPANG);
        Goods goods12 = new Goods("imgUrl12", url, "goods12", 130000, Commerce.COUPANG);
        Goods goods13 = new Goods("imgUrl13", url, "goods13", 140000, Commerce.COUPANG);
        Goods goods14 = new Goods("imgUrl14", url, "goods14", 150000, Commerce.COUPANG);
        goodsRepository.saveAllAndFlush(List.of(goods1, goods2, goods3, goods4, goods5, goods6, goods7, goods8, goods9, goods10, goods11, goods12, goods13, goods14));
        UsersGoods usersGoods1 = UsersGoods.builder().users(savedUsers).goods(goods1).boards(savedBoards).updatedUsersGoodsName(goods1.getGoodsName()).updatedUsersGoodsPrice(goods1.getGoodsPrice()).wishGoods(true).build();
        UsersGoods usersGoods2 = UsersGoods.builder().users(savedUsers).goods(goods1).boards(savedBoards).updatedUsersGoodsName(goods2.getGoodsName()).updatedUsersGoodsPrice(goods2.getGoodsPrice()).wishGoods(true).build();
        UsersGoods usersGoods3 = UsersGoods.builder().users(savedUsers).goods(goods1).boards(savedBoards).updatedUsersGoodsName(goods3.getGoodsName()).updatedUsersGoodsPrice(goods3.getGoodsPrice()).wishGoods(true).build();
        UsersGoods usersGoods4 = UsersGoods.builder().users(savedUsers).goods(goods1).boards(savedBoards).updatedUsersGoodsName(goods4.getGoodsName()).updatedUsersGoodsPrice(goods4.getGoodsPrice()).wishGoods(true).build();
        UsersGoods usersGoods5 = UsersGoods.builder().users(savedUsers).goods(goods1).boards(savedBoards).updatedUsersGoodsName(goods5.getGoodsName()).updatedUsersGoodsPrice(goods5.getGoodsPrice()).wishGoods(true).build();
        UsersGoods usersGoods6 = UsersGoods.builder().users(savedUsers).goods(goods1).boards(savedBoards).updatedUsersGoodsName(goods6.getGoodsName()).updatedUsersGoodsPrice(goods6.getGoodsPrice()).wishGoods(true).build();
        UsersGoods usersGoods7 = UsersGoods.builder().users(savedUsers).goods(goods1).boards(savedBoards).updatedUsersGoodsName(goods7.getGoodsName()).updatedUsersGoodsPrice(goods7.getGoodsPrice()).wishGoods(true).build();
        UsersGoods usersGoods8 = UsersGoods.builder().users(savedUsers).goods(goods1).boards(savedBoards).updatedUsersGoodsName(goods8.getGoodsName()).updatedUsersGoodsPrice(goods8.getGoodsPrice()).wishGoods(true).build();
        UsersGoods usersGoods9 = UsersGoods.builder().users(savedUsers).goods(goods1).boards(savedBoards).updatedUsersGoodsName(goods9.getGoodsName()).updatedUsersGoodsPrice(goods9.getGoodsPrice()).wishGoods(true).build();
        UsersGoods usersGoods10 = UsersGoods.builder().users(savedUsers).goods(goods1).boards(savedBoards).updatedUsersGoodsName(goods10.getGoodsName()).updatedUsersGoodsPrice(goods10.getGoodsPrice()).wishGoods(true).build();
        UsersGoods usersGoods11 = UsersGoods.builder().users(savedUsers).goods(goods1).boards(savedBoards).updatedUsersGoodsName(goods11.getGoodsName()).updatedUsersGoodsPrice(goods11.getGoodsPrice()).wishGoods(true).build();
        UsersGoods usersGoods12 = UsersGoods.builder().users(savedUsers).goods(goods1).boards(savedBoards).updatedUsersGoodsName(goods12.getGoodsName()).updatedUsersGoodsPrice(goods12.getGoodsPrice()).wishGoods(true).build();
        UsersGoods usersGoods13 = UsersGoods.builder().users(savedUsers).goods(goods1).boards(savedBoards).updatedUsersGoodsName(goods13.getGoodsName()).updatedUsersGoodsPrice(goods13.getGoodsPrice()).wishGoods(true).build();
        UsersGoods usersGoods14 = UsersGoods.builder().users(savedUsers).goods(goods1).boards(savedBoards).updatedUsersGoodsName(goods14.getGoodsName()).updatedUsersGoodsPrice(goods14.getGoodsPrice()).wishGoods(true).build();
        List<UsersGoods> usersGoodsList = usersGoodsRepository.saveAllAndFlush(List.of(usersGoods1, usersGoods2, usersGoods3, usersGoods4, usersGoods5, usersGoods6, usersGoods7, usersGoods8, usersGoods9, usersGoods10, usersGoods11, usersGoods12, usersGoods13, usersGoods14));
        // when
        Slice<UsersGoods> usersGoodsSlice = usersGoodsRepository.findByUsersIdAndBoardsIdAndWishGoodsAndIdLessThan(savedUsers.getId(), savedBoards.getId(), true, usersGoodsList.get(9).getId(),  PageRequest.of(0, 4, Sort.by("id").descending()));
        // then
        assertThat(usersGoodsSlice.getSize()).isEqualTo(4);
        assertThat(usersGoodsSlice).extracting("updatedUsersGoodsName", "updatedUsersGoodsPrice")
                .containsExactly(
                        tuple("goods9", 900000),
                        tuple("goods8", 800000),
                        tuple("goods7", 700000),
                        tuple("goods6", 600000)
                );
    }
}















