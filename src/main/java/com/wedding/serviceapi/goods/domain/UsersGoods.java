package com.wedding.serviceapi.goods.domain;

import com.wedding.serviceapi.users.domain.Users;
import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
public class UsersGoods {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "users_goods_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "users_id")
    private Users users;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "goods_id")
    private Goods goods;

    private String updatedUsersGoodsName;
    private String updatedUsersGoodsPrice;
    private Integer usersGoodsTotalDonation;
    private Boolean wishGoods;
}
