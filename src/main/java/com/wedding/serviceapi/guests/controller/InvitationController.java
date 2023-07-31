package com.wedding.serviceapi.guests.controller;

import com.wedding.serviceapi.auth.vo.LoginUser;
import com.wedding.serviceapi.auth.vo.LoginUserVo;
import com.wedding.serviceapi.boards.dto.weddinghall.WeddingHallInfoDto;
import com.wedding.serviceapi.common.vo.ResponseVo;
import com.wedding.serviceapi.gallery.dto.S3ImgInfoDto;
import com.wedding.serviceapi.goods.dto.UsersGoodsInfoDto;
import com.wedding.serviceapi.guests.domain.AttendanceType;
import com.wedding.serviceapi.guests.dto.AttendanceResponseDto;
import com.wedding.serviceapi.guests.dto.UsersGoodsInfoResponseDto;
import com.wedding.serviceapi.guests.service.InvitationService;
import com.wedding.serviceapi.guests.vo.RequestAttendanceVo;
import com.wedding.serviceapi.guests.vo.RequestDonationVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 해당 컨트롤러에서는 초대장을 받은 사용자가 정확한 초대장 페이지에 요청한 것인지를<br>
 * 확인하기 위한 작업이 항상 우선으로 진행되어야 한다.<br>
 * 진행되는 작업의 내용 및 순서는 아래와 같다.<br>
 * <br>
 * <p>
 * 1. cookie에 boardsId가 있는지 일차로 확인 -> 없으면 uuid를 보내야 한다고 응답<br>
 * 2. cookie에 boardsId가 있다면 -> isRegistered 쿠키 값이 true 인지 확인<br>
 * 3. true 라면 그대로 진행, false 라면 cookie 설정도 진행
 * </p>
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/invitation")
@Slf4j
public class InvitationController {

    private final InvitationService invitationService;


    @GetMapping("/gallery/images")
    public ResponseVo<List<S3ImgInfoDto>> findAllGalleryImg(@LoginUser LoginUserVo loginUserVo, HttpServletRequest request) {
        List<S3ImgInfoDto> data = invitationService.findAllGalleryImg(request);

        return ResponseVo.ok(data);
    }

    @GetMapping("/weddingHall/products")
    public ResponseVo<List<UsersGoodsInfoDto>> findAllUsersGoods(@LoginUser LoginUserVo loginUserVo, HttpServletRequest request) {
        List<UsersGoodsInfoDto> data = invitationService.findAllUsersGoods(request);

        return ResponseVo.ok(data);
    }

    @GetMapping("/weddingHall/info")
    public ResponseVo<WeddingHallInfoDto> findWeddingHallInfo(@LoginUser LoginUserVo loginUserVo, HttpServletRequest request) {
        WeddingHallInfoDto data = invitationService.findWeddingHallInfo(request);

        return ResponseVo.ok(data);
    }

    @GetMapping("/weddingHall/attendance")
    public ResponseVo<AttendanceResponseDto> getAttendance(@LoginUser LoginUserVo loginUserVo, HttpServletRequest request) {
        AttendanceResponseDto data = invitationService.getAttendance(request, loginUserVo.getUserId());

        return ResponseVo.ok(data);
    }

    @PostMapping("/weddingHall/attendance")
    public ResponseVo<AttendanceResponseDto> checkAttendance(@LoginUser LoginUserVo loginUserVo,
                                                             HttpServletRequest request,
                                                             @RequestBody RequestAttendanceVo requestAttendanceVo) {
        AttendanceType attendanceType = AttendanceType.checkAttendance(requestAttendanceVo.getAttend());
        AttendanceResponseDto data = invitationService.checkAttendance(request, loginUserVo.getUserId(), attendanceType);

        return ResponseVo.accepted(data);
    }

    @PostMapping("/weddingHall/donation")
    public ResponseVo<UsersGoodsInfoResponseDto> postDonation(@LoginUser LoginUserVo loginUserVo,
                                                              HttpServletRequest request,
                                                              @Validated @RequestBody RequestDonationVo requestDonationVo) {
        UsersGoodsInfoResponseDto data = invitationService.donateUsersGoods(request, requestDonationVo.getUsersGoodsId(), requestDonationVo.getDonation(), loginUserVo.getUserId());

        return ResponseVo.accepted(data);
    }
}






















