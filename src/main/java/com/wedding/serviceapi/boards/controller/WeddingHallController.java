package com.wedding.serviceapi.boards.controller;

import com.wedding.serviceapi.boards.dto.WeddingHallAddressDto;
import com.wedding.serviceapi.boards.dto.WeddingHallDateTimeDto;
import com.wedding.serviceapi.boards.service.WeddingHallService;
import com.wedding.serviceapi.boards.vo.RequestPostWeddingHallTimeVo;
import com.wedding.serviceapi.common.dto.ResponseDto;
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

    @PostMapping("/location/{boardsId}")
    public ResponseDto<WeddingHallAddressDto> postWeddingHallAddress(@PathVariable Long boardsId ,@RequestBody String address) {
        log.info("[postWeddingHallAddress controller] boardsId = {}, address = {}", boardsId, address);
        WeddingHallAddressDto data = weddingHallService.postWeddingHallAddress(boardsId, address);

        return new ResponseDto<>(true, HttpStatus.CREATED.value(), data);
    }

    @PostMapping("/time/{boardsId}")
    public ResponseDto<WeddingHallDateTimeDto> postWeddingHallDateTime(@PathVariable Long boardsId, @RequestBody RequestPostWeddingHallTimeVo body) {
        log.info("[postWeddingHallTime controller] boardsId = {}, date = {}, time = {}", boardsId, body.getDate(), body.getTime());
        WeddingHallDateTimeDto data = weddingHallService.postWeddingHallDateTime(boardsId, body.getDate(), body.getTime());

        return new ResponseDto<>(true, HttpStatus.CREATED.value(), data);
    }
}
