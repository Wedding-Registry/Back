package com.wedding.serviceapi.admin.domain;

import com.wedding.serviceapi.admin.dto.memo.WishItemPagingDto;
import com.wedding.serviceapi.goods.domain.UsersGoods;
import com.wedding.serviceapi.goods.dto.UsersGoodsPostResponseDto;
import org.springframework.data.domain.Slice;

import java.util.List;
import java.util.stream.Collectors;

public class PagingManager implements PagingUtil {

    private final Slice<UsersGoods> origin;
    private List<UsersGoods> content;
    private int content_size;

    public PagingManager(Slice<UsersGoods> origin) {
        this.origin = origin;
        setContent();
    }

    private void setContent() {
        this.content = origin.getContent();
        this.content_size = content.size();
    }

    @Override
    public WishItemPagingDto makeWishItemPagingDto() {
        return new WishItemPagingDto(makeWishItemDtoList(), isLast(), getLastId());
    }

    private boolean isLast() {
        return origin.isLast();
    }

    private long getLastId() {
        return content_size == 0 ? -1 : content.get(content_size - 1).getId();
    }

    private List<UsersGoodsPostResponseDto> makeWishItemDtoList() {
        return content_size == 0 ? List.of() : content.stream().map(UsersGoodsPostResponseDto::from).collect(Collectors.toList());
    }
}
