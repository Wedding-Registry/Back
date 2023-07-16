package com.wedding.serviceapi.goods.domain;

import com.wedding.serviceapi.boards.domain.Boards;
import com.wedding.serviceapi.exception.NegativePriceException;
import com.wedding.serviceapi.users.domain.LoginType;
import com.wedding.serviceapi.users.domain.Users;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class UsersGoodsTest {

    private Users users;
    private Goods goods;
    private Boards boards;
    public UsersGoods usersGoods;

    @BeforeEach
    void setUsersAndGoods() {
        users = new Users("email", "password", LoginType.KAKAO);
        goods = new Goods("imgUrl", "url", "goodsName", 10000, Commerce.COUPANG);
        boards = Boards.builder().id(1L).uuidFirst("first").uuidSecond("second").build();
        usersGoods = new UsersGoods(users, goods, boards, false);
    }

    @Test
    @DisplayName(value = "Users와 Goods를 받는 생성자 작동 여부 확인 테스트")
    void constructorTest() {
        // then
        assertThat(usersGoods.getUpdatedUsersGoodsName()).isEqualTo(goods.getGoodsName());
        assertThat(usersGoods.getUpdatedUsersGoodsPrice()).isEqualTo(goods.getGoodsPrice());
    }

    @Test
    @DisplayName(value = "상품이름 변경 메서드 테스트")
    void changeUsersGoodsName() {
        // given
        String newUsersGoodsName = "goodsName2";

        // when
        usersGoods.changeUsersGoodsName(newUsersGoodsName);

        // then
        assertThat(usersGoods.getUpdatedUsersGoodsName()).isEqualTo(newUsersGoodsName);
    }

    @Test
    @DisplayName("상품이름 정보가 null로 들어온 경우")
    void nullUsersGoodsName() {
        // given
        String newUsersGoodsName = null;
        // when
        assertThrows(IllegalArgumentException.class, () -> usersGoods.changeUsersGoodsName(newUsersGoodsName));
    }

    @Test
    @DisplayName("상품이름 정보가 빈 값으로 들어온 경우")
    void blankUsersGoodsName() {
        // given
        String newUsersGoodsName = " ";
        // when
        IllegalArgumentException error = assertThrows(IllegalArgumentException.class, () -> usersGoods.changeUsersGoodsName(newUsersGoodsName));
        assertThat(error.getMessage()).isEqualTo("이름 정보가 필요합니다.");

    }

    @Test
    @DisplayName("상품 후원가 수정 성공")
    void changeUsersGoodsPrice() {
        // given
        Integer newPrice = 100;

        // when
        usersGoods.changeUsersGoodsPrice(newPrice);

        // then
        assertThat(usersGoods.getUpdatedUsersGoodsPrice()).isEqualTo(100);
    }

    @Test
    @DisplayName("상품 후원가 수정 실패")
    void negativePriceException() {
        // given
        Integer newPrice = -100;

        // then
        NegativePriceException error = assertThrows(NegativePriceException.class, () -> usersGoods.changeUsersGoodsPrice(newPrice));
        assertThat(error.getMessage()).isEqualTo("적절하지 않은 상품 가격입니다.");
    }
}













