package com.wedding.serviceapi.util.webclient;

import lombok.*;

@Getter @Setter
@NoArgsConstructor
@ToString
public class GoodsRegisterResponseDto {
    private Integer status;
    private String goodsName;
    private Integer goodsPrice;
    private String goodsImgUrl;

    public GoodsRegisterResponseDto(String goodsName, Integer goodsPrice, String goodsImgUrl) {
        if (goodsName == null || goodsPrice == null || goodsImgUrl == null) {
            this.status = 500;
        } else {
            this.status = 200;
        }
        this.goodsName = goodsName;
        this.goodsPrice = goodsPrice;
        this.goodsImgUrl = goodsImgUrl;
    }
}
