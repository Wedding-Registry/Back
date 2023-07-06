package com.wedding.serviceapi.guests.service;

import com.wedding.serviceapi.boards.dto.weddinghall.WeddingHallInfoDto;
import com.wedding.serviceapi.boards.service.WeddingHallService;
import com.wedding.serviceapi.common.vo.GuestBoardInfoVo;
import com.wedding.serviceapi.goods.dto.UsersGoodsInfoDto;
import com.wedding.serviceapi.goods.service.UsersGoodsService;
import com.wedding.serviceapi.guests.domain.AttendanceType;
import com.wedding.serviceapi.guests.domain.Guests;
import com.wedding.serviceapi.gallery.dto.S3ImgInfoDto;
import com.wedding.serviceapi.gallery.service.GalleryService;
import com.wedding.serviceapi.guests.invitationinfo.GuestInvitationInfoCheck;
import com.wedding.serviceapi.guests.repository.GuestsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional
@RequiredArgsConstructor
public class InvitationService {

    private final GalleryService galleryService;
    private final UsersGoodsService usersGoodsService;
    private final WeddingHallService weddingHallService;
    private final GuestInvitationInfoCheck guestInvitationInfoCheck;
    private final GuestsRepository guestsRepository;

    public List<S3ImgInfoDto> findAllGalleryImg(HttpServletRequest request) {
        GuestBoardInfoVo guestBoardInfo = guestInvitationInfoCheck.getGuestBoardInfo(request);
        Long boardsId = guestBoardInfo.getBoardsId();
        return galleryService.findAllGalleryImg(boardsId);
    }

    public List<UsersGoodsInfoDto> findAllUsersGoods(HttpServletRequest request) {
        GuestBoardInfoVo guestBoardInfo = guestInvitationInfoCheck.getGuestBoardInfo(request);
        Long boardsId = guestBoardInfo.getBoardsId();
        return usersGoodsService.findAllUsersGoods(boardsId);
    }

    public WeddingHallInfoDto findWeddingHallInfo(HttpServletRequest request) {
        GuestBoardInfoVo guestBoardInfo = guestInvitationInfoCheck.getGuestBoardInfo(request);
        Long boardsId = guestBoardInfo.getBoardsId();
        return weddingHallService.getWeddingHallInfo(boardsId);
    }

    // TODO: 2023/07/07 만약 관리자가 관리자 페이지를 보고 있다면 게스트가 참석 여부를 선택할때 어떻게 변경된 값을 나타낼 것인가
    public void checkAttendance(HttpServletRequest request, Long usersId, AttendanceType attendanceType) {
        GuestBoardInfoVo guestBoardInfo = guestInvitationInfoCheck.getGuestBoardInfo(request);
        Long boardsId = guestBoardInfo.getBoardsId();
        Guests guests = guestsRepository.findByUsersIdAndBoardsId(usersId, boardsId).orElseThrow(() -> new NoSuchElementException("해당 사용자는 초대되지 않았습니다."));
        guests.changeAttendanceType(attendanceType);
    }

//    public UsersGoodsInfoResponseDto donateUsersGoods(HttpServletRequest request, HttpServletResponse response, Long usersGoodsId, int donation, Long usersId) {
//        invitationInfoSetter.checkInvitationInfoAndSettingInfoIfNotExist(request, response, usersId);
//        UsersGoods usersGoods = usersGoodsRepository.findById(usersGoodsId).orElseThrow(() -> new NoSuchElementException("해당하는 상품 항목이 없습니다."));
//        usersGoods.donateMoney(donation);
//        return UsersGoodsInfoResponseDto.from(usersGoods);
//    }
}
