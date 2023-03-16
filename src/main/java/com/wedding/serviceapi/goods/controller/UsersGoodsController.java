package com.wedding.serviceapi.goods.controller;

import com.wedding.serviceapi.common.dto.ResponseDto;
import com.wedding.serviceapi.goods.dto.UsersGoodsPostResponseDto;
import com.wedding.serviceapi.goods.service.UsersGoodsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("/usersgoods")
@RequiredArgsConstructor
public class UsersGoodsController {
    // TODO: 2023/03/09 메서드마다 usersId 와 jwt 토큰의 usersId가 같은지 확인

    private final UsersGoodsService usersGoodsService;

    @PostMapping("/add/{userId}")
    public ResponseDto<UsersGoodsPostResponseDto> postUsersGoods(@PathVariable Long userId, @RequestBody String url) {
        log.info("[postUsersGoods controller] userId = {}, url = {}", userId, url);

        UsersGoodsPostResponseDto data = usersGoodsService.postUsersGoods(userId, url);
        return new ResponseDto<>(true, HttpStatus.CREATED.value(), data);
    }

    @PostMapping("/name/update/{userId}")
    public ResponseDto<Void> updateUsersGoodsName(@PathVariable Long userId, @RequestParam Long usersGoodsId, @RequestBody String usersGoodsName) {
        log.info("[updateUsersGoodsName controller] userId = {}, usersGoodsId = {}, usersGoodsName = {}", userId, usersGoodsId, usersGoodsName);

        usersGoodsService.updateUsersGoodsName(usersGoodsId, usersGoodsName);
        return new ResponseDto<>(true, HttpStatus.CREATED.value(), null);
    }

//    @PostMapping("/cost/update/{userId}")
//    public ResponseDto
}
