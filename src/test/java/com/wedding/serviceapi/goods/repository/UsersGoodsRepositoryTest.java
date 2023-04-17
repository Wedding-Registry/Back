package com.wedding.serviceapi.goods.repository;

import com.wedding.serviceapi.goods.domain.Commerce;
import com.wedding.serviceapi.goods.domain.Goods;
import com.wedding.serviceapi.goods.domain.UsersGoods;
import com.wedding.serviceapi.users.domain.LoginType;
import com.wedding.serviceapi.users.domain.Users;
import com.wedding.serviceapi.users.repository.UsersRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import javax.persistence.EntityManager;

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
    EntityManager em;

    public String url;
    public Goods goods;
    public Users users;
    public Users savedUsers;
    public UsersGoods usersGoods;

    @BeforeEach
    void setting() {
        url = "testUrl";
        goods = new Goods("imgUrl", url, "goods1", 100000, Commerce.COUPANG);
        users = Users.builder().email("test").password("password").loginType(LoginType.KAKAO).build();

        savedUsers = usersRepository.save(users);
        goodsRepository.save(goods);
        usersGoods = new UsersGoods(savedUsers, goods);
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
        assertThat(result.getUsers().getEmail()).isEqualTo("test");
    }
}















