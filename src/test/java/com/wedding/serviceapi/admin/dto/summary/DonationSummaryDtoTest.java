package com.wedding.serviceapi.admin.dto.summary;

import com.wedding.serviceapi.goods.domain.UsersGoods;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class DonationSummaryDtoTest {

    @Test
    @DisplayName("도네이션 비율 내림차순 정렬 테스트")
    void sortTest() {
        // given
        UsersGoods goods1 = UsersGoods.builder().id(1L).updatedUsersGoodsName("goods1").updatedUsersGoodsPrice(100).usersGoodsTotalDonation(90).build();
        UsersGoods goods2 = UsersGoods.builder().id(1L).updatedUsersGoodsName("goods1").updatedUsersGoodsPrice(100).usersGoodsTotalDonation(80).build();
        UsersGoods goods3 = UsersGoods.builder().id(1L).updatedUsersGoodsName("goods1").updatedUsersGoodsPrice(100).usersGoodsTotalDonation(95).build();
        UsersGoods goods4 = UsersGoods.builder().id(1L).updatedUsersGoodsName("goods1").updatedUsersGoodsPrice(100).usersGoodsTotalDonation(79).build();
        UsersGoods goods5 = UsersGoods.builder().id(1L).updatedUsersGoodsName("goods1").updatedUsersGoodsPrice(100).usersGoodsTotalDonation(30).build();
        List<UsersGoods> goodsList = List.of(goods1, goods2, goods3, goods4, goods5);
        // when
        List<DonationSummaryDto> testList = goodsList.stream().map(DonationSummaryDto::of)
                .sorted().collect(Collectors.toList());
        // then
        Assertions.assertThat(testList.get(0).getUsersGoodsTotalDonation()).isEqualTo(95);
        Assertions.assertThat(testList.get(1).getUsersGoodsTotalDonation()).isEqualTo(90);
        Assertions.assertThat(testList.get(2).getUsersGoodsTotalDonation()).isEqualTo(80);
        Assertions.assertThat(testList.get(3).getUsersGoodsTotalDonation()).isEqualTo(79);
        Assertions.assertThat(testList.get(4).getUsersGoodsTotalDonation()).isEqualTo(30);

    }
}