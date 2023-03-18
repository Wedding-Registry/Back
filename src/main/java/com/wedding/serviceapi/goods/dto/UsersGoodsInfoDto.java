package com.wedding.serviceapi.goods.dto;

import com.wedding.serviceapi.goods.domain.UsersGoods;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter
@ToString
public class UsersGoodsInfoDto {
    private Long usersGoodsId;
    private String usersGoodsName;
    private Integer usersGoodsPrice;
    private Integer usersGoodsTotalDonation;
    private Integer usersGoodsPercent;
    private String usersGoodsImgUrl;

    public UsersGoodsInfoDto(UsersGoods usersGoods) {
        this.usersGoodsId = usersGoods.getId();
        this.usersGoodsName = usersGoods.getUpdatedUsersGoodsName();
        this.usersGoodsPrice = usersGoods.getUpdatedUsersGoodsPrice();
        this.usersGoodsTotalDonation = usersGoods.getUsersGoodsTotalDonation();
        this.usersGoodsPercent = getDonationPercent(usersGoods.getUpdatedUsersGoodsPrice(), usersGoods.getUsersGoodsTotalDonation());
        this.usersGoodsImgUrl = usersGoods.getGoods().getGoodsImgUrl();
    }

    private Integer getDonationPercent(Integer usersGoodsPrice, Integer usersGoodsTotalDonation) {
        if (usersGoodsTotalDonation == null) return 0;
        return (usersGoodsTotalDonation * 100 / usersGoodsPrice);
    }
}
