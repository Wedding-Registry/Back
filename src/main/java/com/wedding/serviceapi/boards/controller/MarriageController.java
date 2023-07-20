package com.wedding.serviceapi.boards.controller;

import com.wedding.serviceapi.auth.vo.LoginUser;
import com.wedding.serviceapi.auth.vo.LoginUserVo;
import com.wedding.serviceapi.boards.dto.marriage.MarriageBankAccountDto;
import com.wedding.serviceapi.boards.dto.marriage.MarriageNameDto;
import com.wedding.serviceapi.boards.service.MarriageService;
import com.wedding.serviceapi.boards.vo.marriage.PostHusbandOrWifeNameRequestVo;
import com.wedding.serviceapi.boards.vo.marriage.RequestPostMarriageBankAccountVo;
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

    @PostMapping("/{type}/name/")
    public ResponseVo<MarriageNameDto> postHusbandOrWifeName(@PathVariable String type,
                                                             @RequestBody PostHusbandOrWifeNameRequestVo body,
                                                             @LoginUser LoginUserVo loginUserVo) {
        log.info("[postHusbandName controller] type = {}, boardsId = {}, name = {}, userId = {}", type, loginUserVo.getBoardsId(), body.getName(), loginUserVo.getUserId());
        MarriageNameDto data = marriageService.postHusbandOrWifeName(type, loginUserVo.getBoardsId(), body.getName(), loginUserVo.getUserId());

        return new ResponseVo<>(true, HttpStatus.CREATED.value(), data);
    }

    @PostMapping("/{type}/account/")
    public ResponseVo<MarriageBankAccountDto> postBankAndAccount(@PathVariable String type,
                                                                 @RequestBody RequestPostMarriageBankAccountVo body,
                                                                 @LoginUser LoginUserVo loginUserVo) {
        log.info("[postBankAndAccount controller] type = {}, boardsId = {}, bank = {}, account = {}",
                type, loginUserVo.getBoardsId(), body.getBank(), body.getAccount());
        MarriageBankAccountDto data = marriageService.postMarriageBankAndAccount(type, loginUserVo.getBoardsId(), body.getBank(), body.getAccount(), loginUserVo.getUserId());

        return new ResponseVo<>(true, HttpStatus.CREATED.value(), data);
    }

}



















