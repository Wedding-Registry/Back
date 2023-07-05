package com.wedding.serviceapi.guests.service;

import com.wedding.serviceapi.boards.dto.weddinghall.WeddingHallInfoDto;
import com.wedding.serviceapi.boards.service.WeddingHallService;
import com.wedding.serviceapi.exception.NoBoardsIdCookieExistException;
import com.wedding.serviceapi.goods.domain.UsersGoods;
import com.wedding.serviceapi.goods.dto.UsersGoodsInfoDto;
import com.wedding.serviceapi.goods.repository.UsersGoodsRepository;
import com.wedding.serviceapi.goods.service.UsersGoodsService;
import com.wedding.serviceapi.guests.domain.AttendanceType;
import com.wedding.serviceapi.guests.domain.Guests;
import com.wedding.serviceapi.guests.dto.UsersGoodsInfoResponseDto;
import com.wedding.serviceapi.guests.invitationinfo.InvitationInfoSetter;
import com.wedding.serviceapi.gallery.dto.S3ImgInfoDto;
import com.wedding.serviceapi.gallery.service.GalleryService;
import com.wedding.serviceapi.guests.repository.GuestsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class InvitationService {

    private final GalleryService galleryService;
    private final UsersGoodsService usersGoodsService;
    private final WeddingHallService weddingHallService;
    private final InvitationInfoSetter invitationInfoSetter;
    private final GuestsRepository guestsRepository;
    private final UsersGoodsRepository usersGoodsRepository;

    public List<S3ImgInfoDto> findAllGalleryImg(HttpServletRequest request, HttpServletResponse response, Long usersId) {
        invitationInfoSetter.checkInvitationInfoAndSettingInfoIfNotExist(request, response, usersId);
        long boardsId = invitationInfoSetter.getBoardsId(request);
        return galleryService.findAllGalleryImg(boardsId);
    }

    public List<UsersGoodsInfoDto> findAllUsersGoods(HttpServletRequest request, HttpServletResponse response, Long usersId) {
        invitationInfoSetter.checkInvitationInfoAndSettingInfoIfNotExist(request, response, usersId);
        long boardsId = invitationInfoSetter.getBoardsId(request);
        return usersGoodsService.findAllUsersGoods(boardsId);
    }

    public WeddingHallInfoDto findWeddingHallInfo(HttpServletRequest request, HttpServletResponse response, Long usersId) {
        invitationInfoSetter.checkInvitationInfoAndSettingInfoIfNotExist(request, response, usersId);
        long boardsId = invitationInfoSetter.getBoardsId(request);
        return weddingHallService.getWeddingHallInfo(boardsId);
    }

    public void checkAttendance(HttpServletRequest request, HttpServletResponse response, Long usersId, AttendanceType attendanceType) {
        invitationInfoSetter.checkInvitationInfoAndSettingInfoIfNotExist(request, response, usersId);
        long boardsId = invitationInfoSetter.getBoardsId(request);
        // TODO: 2023/06/17 gusetsRepository를 쿠키 체크 부분에서도 이용하게 된다 -> 로직 수정 해보자
        Guests guests = guestsRepository.findByUsersIdAndBoardsId(usersId, boardsId).orElseThrow(() -> new NoSuchElementException("해당 사용자는 초대되지 않았습니다."));
        guests.changeAttendanceType(attendanceType);
    }

    public UsersGoodsInfoResponseDto donateUsersGoods(HttpServletRequest request, HttpServletResponse response, Long usersGoodsId, int donation, Long usersId) {
        invitationInfoSetter.checkInvitationInfoAndSettingInfoIfNotExist(request, response, usersId);
        UsersGoods usersGoods = usersGoodsRepository.findById(usersGoodsId).orElseThrow(() -> new NoSuchElementException("해당하는 상품 항목이 없습니다."));
        usersGoods.donateMoney(donation);
        return UsersGoodsInfoResponseDto.from(usersGoods);
    }
}
