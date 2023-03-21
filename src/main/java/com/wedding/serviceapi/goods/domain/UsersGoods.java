package com.wedding.serviceapi.goods.domain;

import com.wedding.serviceapi.exception.NegativePriceException;
import com.wedding.serviceapi.users.domain.Users;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
@ToString
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
        // TODO: 2023/03/22 에러처리 controller 작성
        if (usersGoodsName == null || usersGoodsName.isBlank()) throw new IllegalArgumentException("이름 정보가 필요합니다.");
        this.updatedUsersGoodsName = usersGoodsName;
    }

    public void changeUsersGoodsPrice(Integer usersGoodsPrice) {
        // TODO: 2023/03/18 에러 처리 controller 작성
        if (usersGoodsPrice == null || usersGoodsPrice < 0) throw new NegativePriceException("적절하지 않은 상품 가격입니다.");
        this.updatedUsersGoodsPrice = usersGoodsPrice;
    }

    public void donateMoney(Integer money) {
        // TODO: 2023/03/18 money 유효성 검사
        this.usersGoodsTotalDonation = this.usersGoodsTotalDonation == null ? money : ++money;
    }
}














