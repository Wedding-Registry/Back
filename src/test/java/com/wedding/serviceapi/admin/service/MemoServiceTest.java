package com.wedding.serviceapi.admin.service;

import com.wedding.serviceapi.admin.dto.memo.WishItemPagingDto;
import com.wedding.serviceapi.boards.domain.Boards;
import com.wedding.serviceapi.boards.repository.BoardsRepository;
import com.wedding.serviceapi.goods.domain.Commerce;
import com.wedding.serviceapi.goods.domain.Goods;
import com.wedding.serviceapi.goods.domain.UsersGoods;
import com.wedding.serviceapi.goods.repository.GoodsRepository;
import com.wedding.serviceapi.goods.repository.UsersGoodsRepository;
import com.wedding.serviceapi.guests.domain.GoodsDonation;
import com.wedding.serviceapi.guests.domain.Guests;
import com.wedding.serviceapi.users.domain.LoginType;
import com.wedding.serviceapi.users.domain.Users;
import com.wedding.serviceapi.users.repository.UsersRepository;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;

import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Slf4j
class MemoServiceTest {

    private final MemoService memoService;
    private final UsersRepository usersRepository;
    private final BoardsRepository boardsRepository;
    private final GoodsRepository goodsRepository;
    private final UsersGoodsRepository usersGoodsRepository;
    private final EntityManager em;

    @Autowired
    public MemoServiceTest(MemoService memoService, UsersRepository usersRepository, BoardsRepository boardsRepository, GoodsRepository goodsRepository, UsersGoodsRepository usersGoodsRepository, EntityManager em) {
        this.memoService = memoService;
        this.usersRepository = usersRepository;
        this.boardsRepository = boardsRepository;
        this.goodsRepository = goodsRepository;
        this.usersGoodsRepository = usersGoodsRepository;
        this.em = em;
    }

    private Boards savedBoards;
    private Users savedUsers;
    private UsersGoods usersGoods;

    @BeforeEach
    void init() {
        Goods goods1 = new Goods("imgUrl1", "goodsUrl1", "goods1", 100000, Commerce.NAVER);
        Goods goods2 = new Goods("imgUrl2", "goodsUrl2", "goods2", 200000, Commerce.NAVER);
        Users users = Users.builder().email("test").password("password").loginType(LoginType.KAKAO).build();
        Boards boards = Boards.builder().users(users).uuidFirst("first").uuidSecond("second").build();

        savedUsers = usersRepository.save(users);
        savedBoards = boardsRepository.save(boards);
        goodsRepository.save(goods1);
        goodsRepository.save(goods2);

        UsersGoods usersGoods1 = UsersGoods.builder().users(savedUsers).goods(goods1).boards(savedBoards).updatedUsersGoodsName(goods1.getGoodsName()).updatedUsersGoodsPrice(goods1.getGoodsPrice()).wishGoods(true).build();
        UsersGoods usersGoods2 = UsersGoods.builder().users(savedUsers).goods(goods2).boards(savedBoards).updatedUsersGoodsName(goods2.getGoodsName()).updatedUsersGoodsPrice(goods2.getGoodsPrice()).wishGoods(true).build();
        UsersGoods usersGoods3 = UsersGoods.builder().users(savedUsers).goods(goods1).boards(savedBoards).updatedUsersGoodsName(goods1.getGoodsName()).updatedUsersGoodsPrice(goods1.getGoodsPrice()).wishGoods(true).build();
        UsersGoods usersGoods4 = UsersGoods.builder().users(savedUsers).goods(goods2).boards(savedBoards).updatedUsersGoodsName(goods2.getGoodsName()).updatedUsersGoodsPrice(goods2.getGoodsPrice()).wishGoods(true).build();
        UsersGoods usersGoods5 = UsersGoods.builder().users(savedUsers).goods(goods1).boards(savedBoards).updatedUsersGoodsName(goods1.getGoodsName()).updatedUsersGoodsPrice(goods1.getGoodsPrice()).wishGoods(true).build();
        UsersGoods usersGoods6 = UsersGoods.builder().users(savedUsers).goods(goods2).boards(savedBoards).updatedUsersGoodsName(goods2.getGoodsName()).updatedUsersGoodsPrice(goods2.getGoodsPrice()).wishGoods(true).build();
        UsersGoods usersGoods7 = UsersGoods.builder().users(savedUsers).goods(goods1).boards(savedBoards).updatedUsersGoodsName(goods1.getGoodsName()).updatedUsersGoodsPrice(goods1.getGoodsPrice()).wishGoods(true).build();
        usersGoods = UsersGoods.builder().users(savedUsers).goods(goods2).boards(savedBoards).updatedUsersGoodsName(goods2.getGoodsName()).updatedUsersGoodsPrice(goods2.getGoodsPrice()).wishGoods(true).build();
        usersGoodsRepository.saveAllAndFlush(List.of(usersGoods1, usersGoods2, usersGoods3, usersGoods4, usersGoods5, usersGoods6, usersGoods7, usersGoods));

    }

    @Test
    @DisplayName("slice 객체 테스트")
    void sliceTest() {
        // given
        Sort sort = Sort.by(Sort.Direction.DESC, "id");
        Pageable pageable = PageRequest.of(0, 2, sort);
        // when
        WishItemPagingDto result = memoService.getAllItemWish(savedUsers.getId(), savedBoards.getId(), usersGoods.getId(), pageable);
        // then
        assertThat(result.getContent().size()).isEqualTo(2);
        assertThat(result.getIsLast()).isFalse();
    }

}