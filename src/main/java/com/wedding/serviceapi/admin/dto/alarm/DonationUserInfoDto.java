package com.wedding.serviceapi.admin.dto.alarm;

import com.wedding.serviceapi.goods.domain.UsersGoods;
import com.wedding.serviceapi.guests.domain.GoodsDonation;
import com.wedding.serviceapi.users.domain.Users;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.format.DateTimeFormatter;

@AllArgsConstructor
@Getter
public class DonationUserInfoDto {
    private Long goodsDonationId;
    private String name;
    private String date;
    private String time;
    private String goods;
    private Integer amount;

    public static DonationUserInfoDto from(GoodsDonation goodsDonation) {
        Users users = goodsDonation.getGuests().getUsers();
        UsersGoods usersGoods = goodsDonation.getUsersGoods();
        String date = goodsDonation.getUpdatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String time = goodsDonation.getUpdatedAt().format(DateTimeFormatter.ofPattern("HH:mm"));
        return new DonationUserInfoDto(goodsDonation.getId(), users.getName(), date, time, usersGoods.getUpdatedUsersGoodsName(), goodsDonation.getGoodsDonationAmount());
    }
}
