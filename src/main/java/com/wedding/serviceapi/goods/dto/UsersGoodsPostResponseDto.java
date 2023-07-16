package com.wedding.serviceapi.goods.dto;

import com.wedding.serviceapi.goods.domain.UsersGoods;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
public class UsersGoodsPostResponseDto {
    private Long usersGoodsId;
    private String usersGoodsImgUrl;
    private String usersGoodsName;
    private Integer usersGoodsPrice;

    public static UsersGoodsPostResponseDto from(UsersGoods usersGoods) {
        return new UsersGoodsPostResponseDto(
                usersGoods.getId(),
                usersGoods.getGoods().getGoodsImgUrl(),
                usersGoods.getUpdatedUsersGoodsName(),
                usersGoods.getUpdatedUsersGoodsPrice()
        );
    }

    public UsersGoodsPostResponseDto(Long usersGoodsId, String usersGoodsImgUrl, String usersGoodsName, Integer usersGoodsPrice) {
        this.usersGoodsId = usersGoodsId;
        this.usersGoodsImgUrl = usersGoodsImgUrl;
        this.usersGoodsName = usersGoodsName;
        this.usersGoodsPrice = usersGoodsPrice;
    }
}
