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
        log.info("[makeNewBoard controller] userId = {}, userName = {}", loginUserVo.getUserId(), loginUserVo.getUserName());
        MakeBoardResponseDto data = usersGoodsService.makeWeddingBoard(loginUserVo.getUserId(), loginUserVo.getUserName());

        return new ResponseVo<>(true, HttpStatus.CREATED.value(), data);
    }

    @GetMapping("/all")
    public ResponseVo<List<UsersGoodsInfoDto>> findAllUsersGoods(@LoginUser LoginUserVo loginUserVo) {
        log.info("[findAllUsersGoods controller] userId = {}, boardId = {}", loginUserVo.getUserId(), loginUserVo.getBoardsId());
        List<UsersGoodsInfoDto> data = usersGoodsService.findAllUsersGoods(loginUserVo.getUserId(), loginUserVo.getBoardsId());
        return new ResponseVo<>(true, HttpStatus.OK.value(), data);
    }

    @PostMapping("/add/product")
    public ResponseVo<UsersGoodsPostResponseDto> postUsersGoods(@LoginUser LoginUserVo loginUserVo,
                                                                @Validated @RequestBody PostUsersGoodsRequestVo body) {
        log.info("[postUsersGoods controller] userId = {}, url = {}, boardId = {}", loginUserVo.getUserId(), body.getUrl(), loginUserVo.getBoardsId());

        UsersGoodsPostResponseDto data = usersGoodsService.postUsersGoods(loginUserVo.getUserId(), body.getUrl(), loginUserVo.getBoardsId());
        return new ResponseVo<>(true, HttpStatus.CREATED.value(), data);
    }

    @PostMapping("/name/update")
    public ResponseVo<UsersGoodsNameDto> updateUsersGoodsName(@LoginUser LoginUserVo loginUserVo, @RequestParam Long usersGoodsId, @RequestBody UpdateUsersGoodsNameRequestVo body) {
        log.info("[updateUsersGoodsName controller] userId = {}, usersGoodsId = {}, usersGoodsName = {}", loginUserVo.getUserId(), usersGoodsId, body.getUsersGoodsName());

        UsersGoodsNameDto data = usersGoodsService.updateUsersGoodsName(loginUserVo.getUserId(), usersGoodsId, body.getUsersGoodsName());
        return new ResponseVo<>(true, HttpStatus.CREATED.value(), data);
    }

    @PostMapping("/cost/update")
    public ResponseVo<UsersGoodsPriceDto> updateUsersGoodsPrice(@LoginUser LoginUserVo loginUserVo, @RequestParam Long usersGoodsId, @RequestBody UpdateUsersGoodsPriceRequestVo body) {
        log.info("[updateUsersGoodsPrice controller] userId = {}, usersGoodsId = {}, usersGoodsPrice = {}", loginUserVo.getUserId(), usersGoodsId, body.getUsersGoodsPrice());

        UsersGoodsPriceDto data = usersGoodsService.updateUsersGoodsPrice(loginUserVo.getUserId(), usersGoodsId, body.getUsersGoodsPrice());
        return new ResponseVo<>(true, HttpStatus.CREATED.value(), data);
    }

    @DeleteMapping()
    public ResponseVo<Void> deleteUsersGoods(@RequestParam Long usersGoodsId) {
        log.info("[deleteUsersGoods controller] usersGoodsId = {}", usersGoodsId);

        usersGoodsService.deleteUsersGoods(usersGoodsId);
        return new ResponseVo<>(true, HttpStatus.ACCEPTED.value(), null);
    }
}

























