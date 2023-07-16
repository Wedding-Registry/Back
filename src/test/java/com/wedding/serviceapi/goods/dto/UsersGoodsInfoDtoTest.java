package com.wedding.serviceapi.goods.dto;

import com.wedding.serviceapi.boards.domain.Boards;
import com.wedding.serviceapi.goods.domain.Commerce;
import com.wedding.serviceapi.goods.domain.Goods;
import com.wedding.serviceapi.goods.domain.UsersGoods;
import com.wedding.serviceapi.users.domain.LoginType;
import com.wedding.serviceapi.users.domain.Users;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@Slf4j
class UsersGoodsInfoDtoTest {

    @Test
    @DisplayName("퍼센트 설정 확인")
    void percentCalculation() {
        // given
        Users user = new Users("test", "test", LoginType.KAKAO);
        Goods goods1 = new Goods("goods1", "goods1", "goods1", 10000, Commerce.COUPANG);
        Boards board = Boards.builder().uuidFirst("first").uuidSecond("second").build();
        UsersGoods usersGoods1 = new UsersGoods(user, goods1, board, false);
        usersGoods1.donateMoney(3340);

        // when
        UsersGoodsInfoDto usersGoodsInfoDto = new UsersGoodsInfoDto(usersGoods1);

        // then
        assertThat(usersGoodsInfoDto.getUsersGoodsPercent()).isEqualTo(33);
    }

}