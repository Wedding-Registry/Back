package com.wedding.serviceapi.guests.service;

import com.wedding.serviceapi.boards.domain.Boards;
import com.wedding.serviceapi.boards.repository.BoardsRepository;
import com.wedding.serviceapi.exception.NoBoardsIdCookieExistException;
import com.wedding.serviceapi.gallery.domain.GalleryImg;
import com.wedding.serviceapi.gallery.dto.S3ImgInfoDto;
import com.wedding.serviceapi.gallery.repository.GalleryImgRepository;
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

    @Test
    @DisplayName("쿠키에 boardsId 값이 세팅되어 있지 않은경우 uuid를 요청한다.")
    void cookieNotSetting() {
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
}