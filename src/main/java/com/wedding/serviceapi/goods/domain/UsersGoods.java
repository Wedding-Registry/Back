package com.wedding.serviceapi.goods.domain;

import com.wedding.serviceapi.users.domain.Users;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
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
    private Integer updatedUsersGoodsPrice;
    private Integer usersGoodsTotalDonation;
    private Boolean wishGoods;

    public UsersGoods(Users users, Goods goods) {
        this.users = users;
        this.goods = goods;
        this.updatedUsersGoodsName = goods.getGoodsName();
        this.updatedUsersGoodsPrice = goods.getGoodsPrice();
    }


    public void changeUsersGoodsName(String usersGoodsName) {
        this.updatedUsersGoodsName = usersGoodsName;
    }
}














