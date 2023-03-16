package com.wedding.serviceapi.goods.domain;

import com.wedding.serviceapi.users.domain.LoginType;
import com.wedding.serviceapi.users.domain.Users;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class UsersGoodsTest {

    private Users users;
    private Goods goods;

    @BeforeEach
    void setUsersAndGoods() {
        users = new Users("email", "password", LoginType.KAKAO);
        goods = new Goods("imgUrl", "url", "goodsName", 10000, Commerce.COUPANG);

    }

    @Test
    @DisplayName(value = "Users와 Goods를 받는 생성자 작동 여부 확인 테스트")
    void constructorTest() {
        // when
        UsersGoods usersGoods = new UsersGoods(users, goods);

        // then
        assertThat(usersGoods.getUpdatedUsersGoodsName()).isEqualTo(goods.getGoodsName());
        assertThat(usersGoods.getUpdatedUsersGoodsPrice()).isEqualTo(goods.getGoodsPrice());
    }

    @Test
    @DisplayName(value = "상품이름 변경 메서드 테스트")
    void changeUsersGoodsName() {
        // given
        UsersGoods usersGoods = new UsersGoods(users, goods);
        String newUsersGoodsName = "goodsName2";

        // when
        usersGoods.changeUsersGoodsName(newUsersGoodsName);

        // then
        assertThat(usersGoods.getUpdatedUsersGoodsName()).isEqualTo(newUsersGoodsName);
    }

    @Test
    void changeUsersGoodsNameWithSaveName() {
        // given
        UsersGoods usersGoods = new UsersGoods(users, goods);
        String newUsersGoodsName = usersGoods.getUpdatedUsersGoodsName();

        // when
        usersGoods.changeUsersGoodsName(newUsersGoodsName);

        // then
        assertThat(usersGoods.getUpdatedUsersGoodsName()).isEqualTo(newUsersGoodsName);
    }
}













