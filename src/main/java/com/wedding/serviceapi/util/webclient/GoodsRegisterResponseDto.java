package com.wedding.serviceapi.util.webclient;

import lombok.*;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class GoodsRegisterResponseDto {
    private Integer status;
    private String goodsName;
    private Integer goodsPrice;
    private String goodsImgUrl;
}
