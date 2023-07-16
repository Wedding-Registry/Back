package com.wedding.serviceapi.guests.dto;

import com.wedding.serviceapi.goods.domain.UsersGoods;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class UsersGoodsInfoResponseDto {
    private Long usersGoodsId;
    private String usersGoodsName;
    private Integer usersGoodsPrice;
    private Integer usersGoodsTotalDonation;
    private Integer usersGoodsPercent;
    private String usersGoodsImgUrl;

    @Builder
    private UsersGoodsInfoResponseDto(UsersGoods usersGoods) {
        this.usersGoodsId = usersGoods.getId();
        this.usersGoodsName = usersGoods.getUpdatedUsersGoodsName();
        this.usersGoodsPrice = usersGoods.getUpdatedUsersGoodsPrice();
        this.usersGoodsTotalDonation = usersGoods.getUsersGoodsTotalDonation();
        this.usersGoodsPercent = calculateDonationPercent(usersGoods.getUpdatedUsersGoodsPrice(), usersGoods.getUsersGoodsTotalDonation());
        this.usersGoodsImgUrl = usersGoods.getGoods().getGoodsImgUrl();
    }

    public static UsersGoodsInfoResponseDto from(UsersGoods usersGoods) {
        return new UsersGoodsInfoResponseDto(usersGoods);
    }

    private Integer calculateDonationPercent(Integer usersGoodsPrice, Integer usersGoodsTotalDonation) {
        if (usersGoodsTotalDonation == null) return 0;
        return (usersGoodsTotalDonation * 100 / usersGoodsPrice);
    }
}
