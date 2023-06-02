package com.wedding.serviceapi.admin.service;

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

    public Slice<UsersGoods> getAllItemWish(Long usersId, Long boardsId, Pageable pageable) {
        Slice<UsersGoods> usersGoods = usersGoodsRepository.findByUsersIdAndBoardsIdAndIdLessThan(usersId, boardsId, 5L, pageable);
        return usersGoods;
    }
}
