package com.wedding.serviceapi.boards.controller;

import com.wedding.serviceapi.boards.dto.marriage.MarriageBankAccountDto;
import com.wedding.serviceapi.boards.dto.marriage.MarriageNameDto;
import com.wedding.serviceapi.boards.service.MarriageService;
import com.wedding.serviceapi.boards.vo.RequestPostMarriageBankAccountVo;
import com.wedding.serviceapi.common.vo.ResponseVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/marriage")
public class MarriageController {

    private final MarriageService marriageService;

    @PostMapping("/{type}/name/{boardsId}")
    public ResponseVo<MarriageNameDto> postHusbandOrWifeName(@PathVariable String type, @PathVariable Long boardsId, @RequestBody String name) {
        log.info("[postHusbandName controller] type = {}, boardsId = {}, name = {}", type, boardsId, name);
        MarriageNameDto data = marriageService.postHusbandOrWifeName(type, boardsId, name);

        return new ResponseVo<>(true, HttpStatus.CREATED.value(), data);
    }

    @PostMapping("/{type}/account/{boardsId}")
    public ResponseVo<MarriageBankAccountDto> postBankAndAccount(@PathVariable String type,
                                                                 @PathVariable Long boardsId,
                                                                 @RequestBody RequestPostMarriageBankAccountVo body) {
        log.info("[postBankAndAccount controller] type = {}, boardsId = {}, bank = {}, account = {}",
                type, boardsId, body.getBank(), body.getAccount());
        MarriageBankAccountDto data = marriageService.postMarriageBankAndAccount(type, boardsId, body.getBank(), body.getAccount());

        return new ResponseVo<>(true, HttpStatus.CREATED.value(), data);
    }

}



















