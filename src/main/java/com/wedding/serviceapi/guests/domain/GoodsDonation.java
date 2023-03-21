package com.wedding.serviceapi.guests.domain;

import com.wedding.serviceapi.goods.domain.UsersGoods;
import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
public class GoodsDonation {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "goods_donation_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "guests_id")
    private Guests guests;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "users_goods_id")
    private UsersGoods usersGoods;

    private Integer goodsDonationAmount;
}