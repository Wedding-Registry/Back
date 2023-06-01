package com.wedding.serviceapi.admin.dto.donation;

import com.wedding.serviceapi.goods.domain.UsersGoods;
import com.wedding.serviceapi.guests.domain.GoodsDonation;
import com.wedding.serviceapi.users.domain.Users;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Getter
@AllArgsConstructor
public class DonatedUsersGoodsInfoDto {
    private Long usersGoodsId;
    private String updatedUsersGoodsName;
    private Integer updatedUsersGoodsPrice;
    private String goodsImgUrl;
    private List<DonationGuestInfoDto> donationList;

    public static DonatedUsersGoodsInfoDto from(UsersGoods usersGoods, Map<Long, Users> usersMap) {
        List<GoodsDonation> goodsDonationList = usersGoods.getDonationList();
        List<DonationGuestInfoDto> donationGuestInfoList = new ArrayList<>();
        goodsDonationList.forEach(goodsDonation -> {
            Long usersId = goodsDonation.getGuests().getUsers().getId();
            Users users = usersMap.get(usersId);
            donationGuestInfoList.add(DonationGuestInfoDto.from(goodsDonation, users));
        });

        return new DonatedUsersGoodsInfoDto(usersGoods.getId(), usersGoods.getUpdatedUsersGoodsName(),
                usersGoods.getUpdatedUsersGoodsPrice(),
                usersGoods.getGoods().getGoodsImgUrl(),
                donationGuestInfoList
        );
    }
}
