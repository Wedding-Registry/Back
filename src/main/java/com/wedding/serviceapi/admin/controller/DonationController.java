package com.wedding.serviceapi.admin.controller;

import com.wedding.serviceapi.admin.dto.donation.AccountTransferInfoDto;
import com.wedding.serviceapi.admin.dto.donation.DonatedUsersGoodsInfoDto;
import com.wedding.serviceapi.admin.service.DonationService;
import com.wedding.serviceapi.admin.vo.AccountTransferRequestVo;
import com.wedding.serviceapi.auth.vo.LoginUser;
import com.wedding.serviceapi.auth.vo.LoginUserVo;
import com.wedding.serviceapi.common.vo.ResponseVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/admin/donation")
public class DonationController {

    private final DonationService donationService;

    @GetMapping("/product/detail")
    public ResponseVo<List<DonatedUsersGoodsInfoDto>> getAllDonationList(@LoginUser LoginUserVo loginUserVo) {
        log.info("[getAllDonationList controller] usersId = {}, boardsId = {}", loginUserVo.getUserId(), loginUserVo.getBoardsId());
        List<DonatedUsersGoodsInfoDto> data = donationService.findAllUsersGoodsInfo(loginUserVo.getUserId(), loginUserVo.getBoardsId());

        return ResponseVo.ok(data);
    }

    @GetMapping("/transfer/detail")
    public ResponseVo<List<AccountTransferInfoDto>> getAllAccountTransferList(@LoginUser LoginUserVo loginUserVo) {
        log.info("[getAllAccountTransferList controller] boardsId = {}", loginUserVo.getBoardsId());
        List<AccountTransferInfoDto> data = donationService.findAllAccountTransferInfo(loginUserVo.getBoardsId());

        return ResponseVo.ok(data);
    }

    @PostMapping("/transfer/detail")
    public ResponseVo<AccountTransferInfoDto> postAccountTransfer(@LoginUser LoginUserVo loginUserVo,
                                                                  @Validated @RequestBody AccountTransferRequestVo accountTransferRequestVo) {
        log.info("[postAccountTransfer controller] usersId = {}, boardsId = {}", loginUserVo.getUserId(), loginUserVo.getBoardsId());
        AccountTransferInfoDto data = donationService.postAccountTransferMemo(loginUserVo.getBoardsId(), accountTransferRequestVo.getTransferMemo());

        return ResponseVo.created(data);
    }

    @PutMapping("/transfer/detail")
    public ResponseVo<AccountTransferInfoDto> putAccountTransfer(@LoginUser LoginUserVo loginUserVo,
                                                                 @Validated @RequestBody AccountTransferInfoDto accountTransferInfoDto) {
        log.info("[putAccountTransfer controller] usersId = {}, boardsId = {}", loginUserVo.getUserId(), loginUserVo.getBoardsId());
        AccountTransferInfoDto data = donationService.putAccountTransferMemo(loginUserVo.getBoardsId(),
                accountTransferInfoDto.getAccountTransferId(), accountTransferInfoDto.getTransferMemo());

        return ResponseVo.accepted(data);
    }

    @DeleteMapping("/transfer/detail")
    public ResponseVo<Void> deleteAccountTransfer(@LoginUser LoginUserVo loginUserVo, @RequestParam() Long accountTransferId) {
        log.info("[deleteAccountTransfer controller] usersId = {}, boardsId = {}", loginUserVo.getUserId(), loginUserVo.getBoardsId());
        donationService.deleteAccountTransferMemo(loginUserVo.getBoardsId(), accountTransferId);

        return ResponseVo.accepted();
    }









}
