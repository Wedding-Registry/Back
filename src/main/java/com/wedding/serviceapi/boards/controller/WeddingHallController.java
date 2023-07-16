package com.wedding.serviceapi.boards.controller;

import com.wedding.serviceapi.auth.vo.LoginUser;
import com.wedding.serviceapi.auth.vo.LoginUserVo;
import com.wedding.serviceapi.boards.dto.weddinghall.WeddingHallAddressDto;
import com.wedding.serviceapi.boards.dto.weddinghall.WeddingHallDateTimeDto;
import com.wedding.serviceapi.boards.dto.weddinghall.WeddingHallInfoDto;
import com.wedding.serviceapi.boards.service.WeddingHallService;
import com.wedding.serviceapi.boards.vo.weddinghall.PostWeddingHallAddressRequestVo;
import com.wedding.serviceapi.boards.vo.weddinghall.RequestPostWeddingHallTimeVo;
import com.wedding.serviceapi.common.vo.ResponseVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping("/weddingHall")
public class WeddingHallController {

    private final WeddingHallService weddingHallService;

    @GetMapping("/all/{boardsId}")
    public ResponseVo<WeddingHallInfoDto> getWeddingHallInfo(@PathVariable Long boardsId, @LoginUser LoginUserVo loginUserVo) {
        log.info("[getWeddingHallInfo controller] boardsId = {}, usersId = {}", boardsId, loginUserVo.getUserId());
        WeddingHallInfoDto data = weddingHallService.getWeddingHallInfo(boardsId, loginUserVo.getUserId());

        return new ResponseVo<>(true, HttpStatus.OK.value(), data);
    }

    @PostMapping("/location/{boardsId}")
    public ResponseVo<WeddingHallAddressDto> postWeddingHallAddress(@PathVariable Long boardsId,
                                                                    @RequestBody PostWeddingHallAddressRequestVo body,
                                                                    @LoginUser LoginUserVo loginUserVo) {
        log.info("[postWeddingHallAddress controller] boardsId = {}, address = {}, usersId = {}", boardsId, body.getAddress(), loginUserVo.getUserId());
        WeddingHallAddressDto data = weddingHallService.postWeddingHallAddress(boardsId, body.getAddress(), loginUserVo.getUserId());

        return new ResponseVo<>(true, HttpStatus.CREATED.value(), data);
    }

    @PostMapping("/time/{boardsId}")
    public ResponseVo<WeddingHallDateTimeDto> postWeddingHallDateTime(@PathVariable Long boardsId,
                                                                      @RequestBody RequestPostWeddingHallTimeVo body,
                                                                      @LoginUser LoginUserVo loginUserVo) {
        log.info("[postWeddingHallTime controller] boardsId = {}, date = {}, time = {}, usersId = {}", boardsId, body.getDate(), body.getTime(), loginUserVo.getUserId());
        WeddingHallDateTimeDto data = weddingHallService.postWeddingHallDateTime(boardsId, body.getDate(), body.getTime(), loginUserVo.getUserId());

        return new ResponseVo<>(true, HttpStatus.CREATED.value(), data);
    }
}
