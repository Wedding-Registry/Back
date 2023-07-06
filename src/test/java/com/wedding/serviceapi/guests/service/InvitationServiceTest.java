package com.wedding.serviceapi.guests.service;

import com.wedding.serviceapi.boards.domain.Boards;
import com.wedding.serviceapi.boards.domain.HusbandAndWifeEachInfo;
import com.wedding.serviceapi.boards.dto.weddinghall.WeddingHallInfoDto;
import com.wedding.serviceapi.boards.repository.BoardsRepository;
import com.wedding.serviceapi.common.vo.GuestBoardInfoVo;
import com.wedding.serviceapi.exception.NoGuestBoardsInfoJwtExistException;
import com.wedding.serviceapi.gallery.domain.GalleryImg;
import com.wedding.serviceapi.gallery.dto.S3ImgInfoDto;
import com.wedding.serviceapi.gallery.repository.GalleryImgRepository;
import com.wedding.serviceapi.goods.domain.Goods;
import com.wedding.serviceapi.goods.domain.UsersGoods;
import com.wedding.serviceapi.goods.dto.UsersGoodsInfoDto;
import com.wedding.serviceapi.goods.repository.GoodsRepository;
import com.wedding.serviceapi.goods.repository.UsersGoodsRepository;
import com.wedding.serviceapi.guests.domain.AttendanceType;
import com.wedding.serviceapi.guests.domain.Guests;
import com.wedding.serviceapi.guests.invitationinfo.GuestInvitationInfoCheck;
import com.wedding.serviceapi.guests.dto.UsersGoodsInfoResponseDto;
import com.wedding.serviceapi.guests.repository.GuestsRepository;
import com.wedding.serviceapi.users.domain.LoginType;
import com.wedding.serviceapi.users.domain.Users;
import com.wedding.serviceapi.users.repository.UsersRepository;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.AbstractObjectAssert;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@Slf4j
@Transactional
class InvitationServiceTest {

    @Autowired
    InvitationService invitationService;
    @Autowired
    UsersRepository usersRepository;
    @Autowired
    BoardsRepository boardsRepository;
    @Autowired
    GalleryImgRepository galleryImgRepository;
    @Autowired
    UsersGoodsRepository usersGoodsRepository;
    @Autowired
    GoodsRepository goodsRepository;
    @Autowired
    GuestsRepository guestsRepository;
    @MockBean
    GuestInvitationInfoCheck guestInvitationInfoCheck;

    @Test
    @DisplayName("사진 정보 요청 시 헤더에 boardsId 값이 세팅되어 있지 않은경우 uuid를 요청한다.")
    void cookieNotSettingInGalleryImg() {
        // given
        MockHttpServletRequest request = new MockHttpServletRequest();
        Mockito.doThrow(NoGuestBoardsInfoJwtExistException.class).when(guestInvitationInfoCheck).getGuestBoardInfo(request);
        // when
        assertThatThrownBy(() -> invitationService.findAllGalleryImg(request))
                .isInstanceOf(NoGuestBoardsInfoJwtExistException.class);
    }

    @Test
    @DisplayName("사진 정보 요청을 보내면 헤더에 저장된 boardsId를 통해 사진들을 전달한다.")
    void getGalleryImages() {
        // given
        Users user = Users.builder().name("test").loginType(LoginType.SERVICE).build();
        Users userGuest = Users.builder().name("userGuest").loginType(LoginType.SERVICE).build();
        usersRepository.save(user);
        Users savedGuest = usersRepository.save(userGuest);
        Boards board = Boards.builder().users(user).build();
        Boards savedBoard = boardsRepository.save(board);
        GalleryImg galleryImg1 = GalleryImg.builder().boards(savedBoard).galleryImgUrl("img1").build();
        GalleryImg galleryImg2 = GalleryImg.builder().boards(savedBoard).galleryImgUrl("img2").build();
        galleryImgRepository.saveAll(List.of(galleryImg1, galleryImg2));
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        Mockito.doReturn(new GuestBoardInfoVo(savedBoard.getId(), true)).when(guestInvitationInfoCheck).getGuestBoardInfo(request);
        // when
        List<S3ImgInfoDto> galleryImgList = invitationService.findAllGalleryImg(request);
        // then
        assertThat(galleryImgList.size()).isEqualTo(2);
        assertThat(galleryImgList).extracting("galleryImgUrl")
                .containsExactly("img1", "img2");
    }

    @Test
    @DisplayName("등록한 상품 리스트 요청 시 헤더에 boardsId 값이 세팅되어 있지 않은경우 uuid를 요청한다.")
    void cookieNotSettingInUsersGoods() {
        // given
        MockHttpServletRequest request = new MockHttpServletRequest();
        Mockito.doThrow(NoGuestBoardsInfoJwtExistException.class).when(guestInvitationInfoCheck).getGuestBoardInfo(request);
        // when
        assertThatThrownBy(() -> invitationService.findAllUsersGoods(request))
                .isInstanceOf(NoGuestBoardsInfoJwtExistException.class);
    }

    @Test
    @DisplayName("등록된 상품 정보 요청을 보내면 헤더에 저장된 boardsId를 통해 상품들을 전달한다.")
    @Rollback(value = false)
    void usersGoods() {
        // given
        Users user = Users.builder().name("test").loginType(LoginType.SERVICE).build();
        Users userGuest = Users.builder().name("userGuest").loginType(LoginType.SERVICE).build();
        Users savedUser = usersRepository.save(user);
        Users savedGuest = usersRepository.save(userGuest);
        Boards board = Boards.builder().users(savedUser).build();
        Boards savedBoard = boardsRepository.save(board);

        Goods goods1 = Goods.builder().goodsUrl("goods1").goodsPrice(10000).goodsName("goods1").build();
        Goods goods2 = Goods.builder().goodsUrl("goods2").goodsPrice(30000).goodsName("goods2").build();

        Goods savedGoods1 = goodsRepository.save(goods1);
        Goods savedGoods2 = goodsRepository.save(goods2);


        UsersGoods usersGoods1 = UsersGoods.builder().users(savedUser).boards(savedBoard).goods(savedGoods1).updatedUsersGoodsPrice(10000).updatedUsersGoodsName("goods1").usersGoodsTotalDonation(3000).wishGoods(false).build();
        UsersGoods usersGoods2 = UsersGoods.builder().users(savedUser).boards(savedBoard).goods(savedGoods2).updatedUsersGoodsPrice(30000).updatedUsersGoodsName("goods2").usersGoodsTotalDonation(4000).wishGoods(false).build();
        usersGoodsRepository.save(usersGoods1);
        usersGoodsRepository.save(usersGoods2);

        MockHttpServletRequest request = new MockHttpServletRequest();

        Mockito.doReturn(new GuestBoardInfoVo(savedBoard.getId(), true)).when(guestInvitationInfoCheck).getGuestBoardInfo(request);
        // when
        List<UsersGoodsInfoDto> data = invitationService.findAllUsersGoods(request);
        // then
        assertThat(data).hasSize(2);
        assertThat(data).extracting("usersGoodsName", "usersGoodsPrice", "usersGoodsTotalDonation", "usersGoodsPercent")
                .containsExactly(
                        tuple("goods1", 10000, 3000, 30),
                        tuple("goods2", 30000, 4000, 13)
                );
    }

    @Test
    @DisplayName("웨딩 정보 요청을 보내면 헤더에 저장된 boardsId를 통해 결혼식 정보를 전달한다.")
    void weddingHallInfo() {
        // given
        Users user = Users.builder().name("test").loginType(LoginType.SERVICE).build();
        Users savedUser = usersRepository.save(user);
        Users userGuest = Users.builder().name("userGuest").loginType(LoginType.SERVICE).build();
        Users savedGuest = usersRepository.save(userGuest);
        Boards boards = Boards.builder().users(savedUser).uuidFirst("first").uuidSecond("second").husband(new HusbandAndWifeEachInfo("husband", "신한은행", "110111111"))
                .wife(new HusbandAndWifeEachInfo("wife", "국민은행", "110211212"))
                .address("강남").date("2023-06-17").time("15:30").build();
        Boards savedBoard = boardsRepository.saveAndFlush(boards);

        MockHttpServletRequest request = new MockHttpServletRequest();
        Mockito.doReturn(new GuestBoardInfoVo(savedBoard.getId(), true)).when(guestInvitationInfoCheck).getGuestBoardInfo(request);

        // when
        WeddingHallInfoDto weddingHallInfo = invitationService.findWeddingHallInfo(request);
        // then
        assertThat(weddingHallInfo.getUsers()).hasSize(2);
        assertThat(weddingHallInfo.getUsers()).extracting("gender", "name")
                .containsExactly(
                        tuple("husband", "husband"),
                        tuple("wife", "wife")
                );
        assertThat(weddingHallInfo.getAccount()).hasSize(2);
        assertThat(weddingHallInfo.getAccount()).extracting("gender", "bank", "account")
                .containsExactly(
                        tuple("husband", "신한은행", "110111111"),
                        tuple("wife", "국민은행", "110211212")
                );
        assertThat(weddingHallInfo.getLocation()).isEqualTo("강남");
        assertThat(weddingHallInfo.getWeddingDate()).isEqualTo("2023-06-17");
        assertThat(weddingHallInfo.getWeddingTime()).isEqualTo("15:30");
    }

    @Test
    @DisplayName("초대받은 유저가 참석 여부를 업데이트 합니다.")
    void updateAttendance() {
        // given
        Users user = Users.builder().name("test").loginType(LoginType.SERVICE).build();
        Users savedUser = usersRepository.save(user);
        Users userGuest = Users.builder().name("userGuest").loginType(LoginType.SERVICE).build();
        Users savedGuest = usersRepository.save(userGuest);
        Boards boards = Boards.builder().users(savedUser).uuidFirst("first").uuidSecond("second").husband(new HusbandAndWifeEachInfo("husband", "신한은행", "110111111"))
                .wife(new HusbandAndWifeEachInfo("wife", "국민은행", "110211212"))
                .address("강남").date("2023-06-17").time("15:30").build();
        Boards savedBoard = boardsRepository.saveAndFlush(boards);
        guestsRepository.save(Guests.builder()
                .boards(savedBoard)
                .users(savedGuest)
                .build());

        MockHttpServletRequest request = new MockHttpServletRequest();
        Mockito.doReturn(new GuestBoardInfoVo(savedBoard.getId(), true)).when(guestInvitationInfoCheck).getGuestBoardInfo(request);
        // when
        invitationService.checkAttendance(request, savedGuest.getId(), AttendanceType.YES);
        // then
        Guests guests = guestsRepository.findByUsersIdAndBoardsId(savedGuest.getId(), savedBoard.getId()).get();
        assertThat(guests.getAttendance()).isEqualTo(AttendanceType.YES);
    }

//    @Test
//    @DisplayName("쿠키에 값이 세팅되어 있지 않다면 게시판 정보를 보내달라는 메시지를 전달한다.")
//    void donateWithoutCookie() {
//        // given
//        MockHttpServletRequest request = new MockHttpServletRequest();
//        MockHttpServletResponse response = new MockHttpServletResponse();
//        // when
//        // then
//        assertThatThrownBy(() -> invitationService.donateUsersGoods(request, response, anyLong(), anyInt(), anyLong()))
//                .isInstanceOf(NoBoardsIdCookieExistException.class)
//                .hasMessage("어떤 게시판인지 알 수 없습니다. 게시판 정보를 보내주세요");
//    }
//
//    @Test
//    @DisplayName("게스트가 특정 상품에 후원하게 되면 해당 상품의 정보와 후원 퍼센티지를 보내준다.")
//    void donateUsersGoods() {
//        // given
//        Users user = Users.builder().name("test").loginType(LoginType.SERVICE).build();
//        Users savedUser = usersRepository.save(user);
//        Users userGuest = Users.builder().name("userGuest").loginType(LoginType.SERVICE).build();
//        Users savedGuest = usersRepository.save(userGuest);
//        Boards boards = Boards.builder().users(savedUser).uuidFirst("first").uuidSecond("second").husband(new HusbandAndWifeEachInfo("husband", "신한은행", "110111111"))
//                .wife(new HusbandAndWifeEachInfo("wife", "국민은행", "110211212"))
//                .address("강남").date("2023-06-17").time("15:30").build();
//        Boards savedBoard = boardsRepository.saveAndFlush(boards);
//        Goods goods = Goods.builder().goodsName("상품1").goodsPrice(10000).goodsImgUrl("test-img").build();
//        Goods savedGoods = goodsRepository.save(goods);
//        UsersGoods usersGoods = UsersGoods.builder().users(savedUser).boards(savedBoard).goods(savedGoods).updatedUsersGoodsName("test상품").usersGoodsTotalDonation(5000).updatedUsersGoodsPrice(10000).build();
//        UsersGoods savedUsersGoods = usersGoodsRepository.save(usersGoods);
//        MockHttpServletRequest request = new MockHttpServletRequest();
//        request.setCookies(new Cookie("boardsId", String.valueOf(savedBoard.getId())));
//        MockHttpServletResponse response = new MockHttpServletResponse();
//        int donationAmount = 2000;
//        // when
//        UsersGoodsInfoResponseDto data = invitationService.donateUsersGoods(request, response, savedUsersGoods.getId(), donationAmount, savedGuest.getId());
//        // then
//        Optional<Guests> savedOptionalGuest = guestsRepository.findByUsersIdAndBoardsId(savedGuest.getId(), savedBoard.getId());
//
//        assertThat(savedOptionalGuest).isNotEmpty();
//        assertThat(savedOptionalGuest.get().getUsers().getName()).isEqualTo("userGuest");
//        assertThat(response.getCookie("isRegistered").getValue()).isEqualTo("true");
//        assertThat(data.getUsersGoodsId()).isEqualTo(savedUsersGoods.getId());
//        assertThat(data.getUsersGoodsName()).isEqualTo("test상품");
//        assertThat(data.getUsersGoodsTotalDonation()).isEqualTo(7000);
//        assertThat(data.getUsersGoodsPercent()).isEqualTo(70);
//    }

}