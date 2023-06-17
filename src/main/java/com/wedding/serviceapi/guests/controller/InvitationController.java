package com.wedding.serviceapi.guests.controller;

import com.wedding.serviceapi.auth.vo.LoginUser;
import com.wedding.serviceapi.auth.vo.LoginUserVo;
import com.wedding.serviceapi.common.vo.ResponseVo;
import com.wedding.serviceapi.gallery.dto.S3ImgInfoDto;
import com.wedding.serviceapi.guests.service.InvitationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
@RequestMapping("/invitation/gallery")
@Slf4j
public class InvitationController {

    private final InvitationService invitationService;


    @GetMapping("/images")
    public ResponseVo<List<S3ImgInfoDto>> findAllGalleryImg(@LoginUser LoginUserVo loginUserVo, HttpServletRequest request, HttpServletResponse response) {
        log.info("[findAllGalleryImg controller] usersId = {}", loginUserVo.getUserId());
        List<S3ImgInfoDto> data = invitationService.findAllGalleryImg(request, response, loginUserVo.getUserId());

        return new ResponseVo<>(true, HttpStatus.OK.value(), data);
    }
}
