package com.wedding.serviceapi.admin.dto.donation;

import com.wedding.serviceapi.guests.domain.GoodsDonation;
import com.wedding.serviceapi.users.domain.Users;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class DonationGuestInfoDto {
    private Long goodsDonationId;
    private Long guestId;
    private String name;
    private Integer amount;

    public static DonationGuestInfoDto from(GoodsDonation goodsDonation, Users users) {
        return new DonationGuestInfoDto(
                goodsDonation.getId(), users.getId(), users.getName(), goodsDonation.getGoodsDonationAmount()
        );
    }
}
