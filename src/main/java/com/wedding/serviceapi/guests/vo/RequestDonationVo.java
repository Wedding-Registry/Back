package com.wedding.serviceapi.guests.vo;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RequestDonationVo {
    @NotNull(message = "상품 번호는 필수입니다.")
    private Long usersGoodsId;
    @Positive(message = "상품 후원 금액이 옳바르지 않습니다.")
    private int donation;

    @Builder
    private RequestDonationVo(Long usersGoodsId, int donation) {
        this.usersGoodsId = usersGoodsId;
        this.donation = donation;
    }
}
