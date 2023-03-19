package com.wedding.serviceapi.goods.controller;

import com.wedding.serviceapi.common.vo.ResponseVo;
import com.wedding.serviceapi.goods.dto.UsersGoodsInfoDto;
import com.wedding.serviceapi.goods.dto.UsersGoodsPostResponseDto;
import com.wedding.serviceapi.goods.service.UsersGoodsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/usersgoods")
@RequiredArgsConstructor
public class UsersGoodsController {
    // TODO: 2023/03/09 메서드마다 usersId 와 jwt 토큰의 usersId가 같은지 확인

    private final UsersGoodsService usersGoodsService;

    @GetMapping("/all/{userId}")
    public ResponseVo<List<UsersGoodsInfoDto>> findAllUsersGoods(@PathVariable Long userId) {
        log.info("[findAllUsersGoods controller] userId = {}", userId);
        List<UsersGoodsInfoDto> data = usersGoodsService.findAllUsersGoods(userId);
        return new ResponseVo<>(true, HttpStatus.OK.value(), data);
    }

    @PostMapping("/add/{userId}")
    public ResponseVo<UsersGoodsPostResponseDto> postUsersGoods(@PathVariable Long userId, @RequestBody String url) {
        log.info("[postUsersGoods controller] userId = {}, url = {}", userId, url);

        UsersGoodsPostResponseDto data = usersGoodsService.postUsersGoods(userId, url);
        return new ResponseVo<>(true, HttpStatus.CREATED.value(), data);
    }

    @PostMapping("/name/update/{userId}")
    public ResponseVo<Void> updateUsersGoodsName(@PathVariable Long userId, @RequestParam Long usersGoodsId, @RequestBody String usersGoodsName) {
        log.info("[updateUsersGoodsName controller] userId = {}, usersGoodsId = {}, usersGoodsName = {}", userId, usersGoodsId, usersGoodsName);

        usersGoodsService.updateUsersGoodsName(userId, usersGoodsId, usersGoodsName);
        return new ResponseVo<>(true, HttpStatus.CREATED.value(), null);
    }

    @PostMapping("/cost/update/{userId}")
    public ResponseVo<Void> updateUsersGoodsPrice(@PathVariable Long userId, @RequestParam Long usersGoodsId, @RequestBody Integer usersGoodsPrice) {
        log.info("[updateUsersGoodsPrice controller] userId = {}, usersGoodsId = {}, usersGoodsPrice = {}", userId, usersGoodsId, usersGoodsPrice);

        usersGoodsService.updateUsersGoodsPrice(userId, usersGoodsId, usersGoodsPrice);
        return new ResponseVo<>(true, HttpStatus.CREATED.value(), null);
    }

    @DeleteMapping()
    public ResponseVo<Void> deleteUsersGoods(@RequestParam Long usersGoodsId) {
        log.info("[deleteUsersGoods controlle] usersGoodsId = {}", usersGoodsId);

        usersGoodsService.deleteUsersGoods(usersGoodsId);
        return new ResponseVo<>(true, HttpStatus.ACCEPTED.value(), null);
    }
}

























