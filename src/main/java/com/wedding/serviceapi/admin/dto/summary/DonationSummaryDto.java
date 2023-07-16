package com.wedding.serviceapi.admin.dto.summary;

import com.wedding.serviceapi.goods.domain.UsersGoods;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class DonationSummaryDto implements Comparable<DonationSummaryDto> {

    private Long usersGoodsId;
    private String usersGoodsName;
    private Integer usersGoodsTotalDonation;
    private long usersGoodsTotalDonationRate;

    public static DonationSummaryDto of(UsersGoods usersGoods) {
        long rate = Math.round(usersGoods.getUsersGoodsTotalDonation() * 100.0 / usersGoods.getUpdatedUsersGoodsPrice());
        return new DonationSummaryDto(usersGoods.getId(), usersGoods.getUpdatedUsersGoodsName(),
                usersGoods.getUsersGoodsTotalDonation(), rate);
    }

    @Override
    public int compareTo(DonationSummaryDto o) {
        return o.usersGoodsTotalDonation - this.usersGoodsTotalDonation;
    }
}
