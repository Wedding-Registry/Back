package com.wedding.serviceapi.goods.vo;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class PostUsersGoodsRequestVo {
    @NotBlank(message = "url 이 필요합니다.")
    private String url;

}
