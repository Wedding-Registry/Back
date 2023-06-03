package com.wedding.serviceapi.admin.service;

import com.wedding.serviceapi.admin.domain.PagingManager;
import com.wedding.serviceapi.admin.dto.memo.WishItemPagingDto;
import com.wedding.serviceapi.goods.domain.UsersGoods;
import com.wedding.serviceapi.goods.repository.UsersGoodsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class MemoService {

    private final UsersGoodsRepository usersGoodsRepository;

    public WishItemPagingDto getAllItemWish(Long usersId, Long boardsId, Long lastId, Pageable pageable) {
        Slice<UsersGoods> usersGoodsSlice = usersGoodsRepository.findByUsersIdAndBoardsIdAndWishGoodsAndIdLessThan(usersId, boardsId, true, lastId, pageable);
        PagingManager pagingManager = new PagingManager(usersGoodsSlice);
        return pagingManager.makeWishItemPagingDto();
    }
}
