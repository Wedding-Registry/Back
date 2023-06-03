package com.wedding.serviceapi.admin.controller;

import com.wedding.serviceapi.admin.dto.memo.WishItemPagingDto;
import com.wedding.serviceapi.admin.service.MemoService;
import com.wedding.serviceapi.auth.vo.LoginUser;
import com.wedding.serviceapi.auth.vo.LoginUserVo;
import com.wedding.serviceapi.common.vo.ResponseVo;
import com.wedding.serviceapi.goods.dto.UsersGoodsPostResponseDto;
import com.wedding.serviceapi.goods.service.UsersGoodsService;
import com.wedding.serviceapi.goods.vo.PostUsersGoodsRequestVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/memo")
@Slf4j
@RequiredArgsConstructor
public class MemoController {

    private final MemoService memoService;
    private final UsersGoodsService usersGoodsService;

    @GetMapping("/item/wish")
    public ResponseVo<WishItemPagingDto> getWishItemList(@LoginUser LoginUserVo loginUserVo,
                                                         @RequestParam("lastId") Long lastId,
                                                         Pageable pageable) {
        log.info("[getWishItemList controller] usersId = {}, boardsId = {}", loginUserVo.getUserId(), loginUserVo.getBoardsId());
        log.info("lastId = {}, size = {}, sort = {}", lastId, pageable.getPageSize(), pageable.getSort());
        WishItemPagingDto data = memoService.getAllItemWish(loginUserVo.getUserId(), loginUserVo.getBoardsId(), lastId, pageable);

        return new ResponseVo<>(true, HttpStatus.OK.value(), data);
    }

    @PostMapping("/item/wish")
    public ResponseVo<UsersGoodsPostResponseDto> postWishItem(@LoginUser LoginUserVo loginUserVo, @Validated @RequestBody PostUsersGoodsRequestVo requestVo) {
        log.info("[postWishItem controller] usersId = {}, boardsId = {}, url ={}", loginUserVo.getUserId(), loginUserVo.getBoardsId(), requestVo.getUrl());
        UsersGoodsPostResponseDto data = usersGoodsService.postUsersGoods(loginUserVo.getUserId(), requestVo.getUrl(), loginUserVo.getBoardsId(), true);

        return new ResponseVo<>(true, HttpStatus.CREATED.value(), data);
    }


}
