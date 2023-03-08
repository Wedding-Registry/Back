package com.wedding.serviceapi.goods.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class UsersGoodsPostResponseDto {
    private Long usersGoodsId;
    private String usersGoodsImgUrl;
    private String usersGoodsName;
    private Integer donation;
}
