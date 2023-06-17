package com.wedding.serviceapi.guests.service;

import com.wedding.serviceapi.boards.domain.Boards;
import com.wedding.serviceapi.boards.domain.HusbandAndWifeEachInfo;
import com.wedding.serviceapi.boards.dto.weddinghall.WeddingHallInfoDto;
import com.wedding.serviceapi.boards.repository.BoardsRepository;
import com.wedding.serviceapi.exception.NoBoardsIdCookieExistException;
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
import com.wedding.serviceapi.guests.repository.GuestsRepository;
import com.wedding.serviceapi.users.domain.LoginType;
import com.wedding.serviceapi.users.domain.Users;
import com.wedding.serviceapi.users.repository.UsersRepository;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.AbstractObjectAssert;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.Cookie;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

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

    @Test
    @DisplayName("사진 정보 요청 시 쿠키에 boardsId 값이 세팅되어 있지 않은경우 uuid를 요청한다.")
    void cookieNotSettingInGalleryImg() {
        // given
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        // when
        assertThatThrownBy(() -> invitationService.findAllGalleryImg(request, response, 1L))
                .isInstanceOf(NoBoardsIdCookieExistException.class)
                .hasMessage("어떤 게시판인지 알 수 없습니다. 게시판 정보를 보내주세요");
    }

    @Test
    @DisplayName("사진 정보 요청을 보내면 쿠키에 저장된 boardsId를 통해 사진들을 전달한다.")
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
        request.setCookies(new Cookie("boardsId", String.valueOf(savedBoard.getId())));
        // when
        List<S3ImgInfoDto> galleryImgList = invitationService.findAllGalleryImg(request, response, savedGuest.getId());
        // then
        assertThat(galleryImgList.size()).isEqualTo(2);
        assertThat(galleryImgList).extracting("galleryImgUrl")
                .containsExactly("img1", "img2");
        assertThat(response.getCookie("isRegistered").getValue()).isEqualTo("true");
    }

    @Test
    @DisplayName("등록한 상품 리스트 요청 시 쿠키에 boardsId 값이 세팅되어 있지 않은경우 uuid를 요청한다.")
    void cookieNotSettingInUsersGoods() {
        // given
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        // when
        assertThatThrownBy(() -> invitationService.findAllUsersGoods(request, response, 1L))
                .isInstanceOf(NoBoardsIdCookieExistException.class)
                .hasMessage("어떤 게시판인지 알 수 없습니다. 게시판 정보를 보내주세요");
    }

    @Test
    @DisplayName("등록된 상품 정보 요청을 보내면 쿠키에 저장된 boardsId를 통해 상품들을 전달한다.")
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
        MockHttpServletResponse response = new MockHttpServletResponse();
        request.setCookies(new Cookie("boardsId", String.valueOf(savedBoard.getId())));

        // when
        List<UsersGoodsInfoDto> data = invitationService.findAllUsersGoods(request, response, savedGuest.getId());
        // then
        assertThat(data).hasSize(2);
        assertThat(data).extracting("usersGoodsName", "usersGoodsPrice", "usersGoodsTotalDonation", "usersGoodsPercent")
                .containsExactly(
                        tuple("goods1", 10000, 3000, 30),
                        tuple("goods2", 30000, 4000, 13)
                );
    }

    @Test
    @DisplayName("웨딩 정보 요청을 보내면 쿠키에 저장된 boardsId를 통해 결혼식 정보를 전달한다.")
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
        MockHttpServletResponse response = new MockHttpServletResponse();
        request.setCookies(new Cookie("boardsId", String.valueOf(savedBoard.getId())));

        // when
        WeddingHallInfoDto weddingHallInfo = invitationService.findWeddingHallInfo(request, response, savedGuest.getId());
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

        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        request.setCookies(new Cookie("boardsId", String.valueOf(savedBoard.getId())));
        // when
        invitationService.checkAttendance(request, response, savedGuest.getId(), AttendanceType.YES);
        // then
        Guests guests = guestsRepository.findByUsersIdAndBoardsId(savedGuest.getId(), savedBoard.getId()).get();
        assertThat(guests.getAttendance()).isEqualTo(AttendanceType.YES);

    }

}