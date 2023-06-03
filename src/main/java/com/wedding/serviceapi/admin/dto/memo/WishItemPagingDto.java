package com.wedding.serviceapi.admin.dto.memo;

import com.wedding.serviceapi.goods.dto.UsersGoodsPostResponseDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@AllArgsConstructor
public class WishItemPagingDto {
    private List<UsersGoodsPostResponseDto> content;
    private Boolean isLast;
    private Long lastId;
}
