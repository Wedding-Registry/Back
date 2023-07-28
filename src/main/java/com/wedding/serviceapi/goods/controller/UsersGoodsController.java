package com.wedding.serviceapi.goods.controller;

import com.wedding.serviceapi.auth.securitycustom.AuthUser;
import com.wedding.serviceapi.auth.vo.LoginUser;
import com.wedding.serviceapi.auth.vo.LoginUserVo;
import com.wedding.serviceapi.common.vo.ResponseVo;
import com.wedding.serviceapi.goods.dto.*;
import com.wedding.serviceapi.goods.service.UsersGoodsService;
import com.wedding.serviceapi.goods.vo.PostUsersGoodsRequestVo;
import com.wedding.serviceapi.goods.vo.UpdateUsersGoodsNameRequestVo;
import com.wedding.serviceapi.goods.vo.UpdateUsersGoodsPriceRequestVo;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/usersgoods")
@RequiredArgsConstructor
public class UsersGoodsController {

    private final UsersGoodsService usersGoodsService;

    @GetMapping("/add/board")
    public ResponseVo<MakeBoardResponseDto> makeNewBoard(@LoginUser LoginUserVo loginUserVo) {
        MakeBoardResponseDto data = usersGoodsService.makeWeddingBoard(loginUserVo.getUserId(), loginUserVo.getUserName());

        return ResponseVo.created(data);
    }

    @GetMapping("/all")
    public ResponseVo<List<UsersGoodsInfoDto>> findAllUsersGoods(@LoginUser LoginUserVo loginUserVo) {
        List<UsersGoodsInfoDto> data = usersGoodsService.findAllUsersGoods(loginUserVo.getUserId(), loginUserVo.getBoardsId());
        return ResponseVo.ok(data);
    }

    @PostMapping("/add/product")
    public ResponseVo<UsersGoodsPostResponseDto> postUsersGoods(@LoginUser LoginUserVo loginUserVo,
                                                                @Validated @RequestBody PostUsersGoodsRequestVo body) {

        UsersGoodsPostResponseDto data = usersGoodsService.postUsersGoods(loginUserVo.getUserId(), body.getUrl(), loginUserVo.getBoardsId(), false);
        return ResponseVo.created(data);
    }

    @PostMapping("/name/update")
    public ResponseVo<UsersGoodsPostResponseDto> updateUsersGoodsName(@LoginUser LoginUserVo loginUserVo, @RequestParam Long usersGoodsId, @RequestBody UpdateUsersGoodsNameRequestVo body) {

        UsersGoodsPostResponseDto data = usersGoodsService.updateUsersGoodsName(loginUserVo.getUserId(), usersGoodsId, body.getUsersGoodsName());
        return ResponseVo.created(data);
    }

    @PostMapping("/cost/update")
    public ResponseVo<UsersGoodsPostResponseDto> updateUsersGoodsPrice(@LoginUser LoginUserVo loginUserVo, @RequestParam Long usersGoodsId, @RequestBody UpdateUsersGoodsPriceRequestVo body) {

        UsersGoodsPostResponseDto data = usersGoodsService.updateUsersGoodsPrice(loginUserVo.getUserId(), usersGoodsId, body.getUsersGoodsPrice());
        return ResponseVo.created(data);
    }

    @DeleteMapping()
    public ResponseVo<Void> deleteUsersGoods(@RequestParam Long usersGoodsId) {

        usersGoodsService.deleteUsersGoods(usersGoodsId);
        return ResponseVo.accepted();
    }
}

























