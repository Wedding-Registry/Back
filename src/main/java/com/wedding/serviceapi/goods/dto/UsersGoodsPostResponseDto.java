package com.wedding.serviceapi.goods.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class UsersGoodsPostResponseDto {
    private Long usersGoodsId;
    private String usersGoodsImgUrl;
    private String usersGoodsName;
    private Integer usersGoodsPrice;
}
