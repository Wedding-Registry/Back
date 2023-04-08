package com.wedding.serviceapi.goods.domain;

import com.wedding.serviceapi.common.domain.BaseEntity;
import com.wedding.serviceapi.util.webclient.GoodsRegisterResponseDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class Goods extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "goods_id")
    private Long id;

    private String goodsImgUrl;
    private String goodsUrl;
    private String goodsName;
    private Integer goodsPrice;

    @Enumerated(value = EnumType.STRING)
    private Commerce commerce;

    public Goods(String goodsImgUrl, String goodsUrl, String goodsName, Integer goodsPrice, Commerce commerce) {
        this.goodsImgUrl = goodsImgUrl;
        this.goodsUrl = goodsUrl;
        this.goodsName = goodsName;
        this.goodsPrice = goodsPrice;
        this.commerce = commerce;
    }

    public void updateGoodsInfo(GoodsRegisterResponseDto goodsInfo) {
        this.goodsImgUrl = goodsInfo.getGoodsImgUrl();
        this.goodsPrice = goodsInfo.getGoodsPrice();
        this.goodsName = goodsInfo.getGoodsName();
    }
}














