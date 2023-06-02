package com.wedding.serviceapi.admin.controller;

import com.wedding.serviceapi.admin.dto.memo.WishItemDto;
import com.wedding.serviceapi.admin.service.MemoService;
import com.wedding.serviceapi.auth.vo.LoginUser;
import com.wedding.serviceapi.auth.vo.LoginUserVo;
import com.wedding.serviceapi.common.vo.ResponseVo;
import com.wedding.serviceapi.goods.domain.UsersGoods;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/admin/memo")
@Slf4j
@RequiredArgsConstructor
public class MemoController {

    private final MemoService memoService;

    @GetMapping("/item/wish")
//    public ResponseVo<List<WishItemDto>> getWishItemList(@LoginUser LoginUserVo loginUserVo, Pageable pageable) {
    public Slice<UsersGoods> getWishItemList(@LoginUser LoginUserVo loginUserVo, Pageable pageable) {
        log.info("[getWishItemList controller] usersId = {}, boardsId = {}", loginUserVo.getUserId(), loginUserVo.getBoardsId());
        log.info("page = {}, size = {}, sort = {}", pageable.getPageNumber(), pageable.getPageSize(), pageable.getSort());
        return memoService.getAllItemWish(loginUserVo.getUserId(), loginUserVo.getBoardsId(), pageable);
//        return "ok";
    }


}
