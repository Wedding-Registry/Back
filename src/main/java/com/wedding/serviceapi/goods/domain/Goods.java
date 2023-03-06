package com.wedding.serviceapi.goods.domain;

import com.wedding.serviceapi.common.domain.BaseEntity;
import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
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
}
