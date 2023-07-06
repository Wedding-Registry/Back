package com.wedding.serviceapi.common.invitationinfo;

import com.wedding.serviceapi.boards.domain.Boards;
import com.wedding.serviceapi.boards.repository.BoardsRepository;
import com.wedding.serviceapi.guests.invitationinfo.CookieUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.http.Cookie;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CookieUtilTest {

    @InjectMocks
    private CookieUtil cookieUtil;

    @Mock
    BoardsRepository boardsRepository;

    @Test
    @DisplayName("요청의 쿠키중 key가 boardsId인 쿠키가 없는 경우")
    void checkBoardsIdFalse() {
        // given
        MockHttpServletRequest request = new MockHttpServletRequest();

        // when
        boolean checkBoardsId = cookieUtil.checkIsBoardsIdExist(request);
        // then
        assertThat(checkBoardsId).isFalse();
    }

    @ParameterizedTest
    @ValueSource(strings = {"null", "", "str"})
    @DisplayName("요청의 쿠키중 key가 boardsId 이지만 value가 빈 값이거나 숫자가 아닌 경우")
    void checkBoardsIdIfCookieValueIsNull(String value) {
        // given
        MockHttpServletRequest request = new MockHttpServletRequest();
        Cookie cookie = new Cookie("boardsId", value);
        request.setCookies(cookie);

        // when
        boolean checkBoardsId = cookieUtil.checkIsBoardsIdExist(request);
        // then
        assertThat(checkBoardsId).isFalse();

    }

    @Test
    @DisplayName("요청의 쿠키중 key가 boardsId인 쿠키가 있는 경우")
    void checkBoardsIdTrue() {
        // given
        MockHttpServletRequest request = new MockHttpServletRequest();
        Cookie cookie = new Cookie("boardsId", "1");
        request.setCookies(cookie);

        // when
        boolean checkBoardsId = cookieUtil.checkIsBoardsIdExist(request);
        // then
        assertThat(checkBoardsId).isTrue();
    }

    @Test
    @DisplayName("uuid 2개가 정상적으로 들어온 경우 해당하는 게시판 id를 쿠키에 세팅한다.")
    void setBoardsId() {
        // given
        Boards boards = Boards.builder().id(1L).uuidFirst("first").uuidSecond("second").build();
        Mockito.doReturn(Optional.of(boards)).when(boardsRepository).findByUuidFirstAndUuidSecond("first", "second");
        MockHttpServletResponse response = new MockHttpServletResponse();
        MockHttpServletRequest request = new MockHttpServletRequest();
        // when
        cookieUtil.setBoardsId(request, response, "first", "second");
        // then
        assertThat(response.getCookie("boardsId").getValue()).isEqualTo("1");
    }

    @Test
    @DisplayName("쿠키에 boardsId 값이 있을때 정상적으로 가져온다.")
    void getBoardsId() {
        // given
        MockHttpServletRequest request = new MockHttpServletRequest();
        Cookie cookie = new Cookie("boardsId", "1");
        request.setCookies(cookie);
        // when
        long boardsId = cookieUtil.getBoardsId(request);
        // then
        assertThat(boardsId).isEqualTo(1L);
    }

    @Test
    @DisplayName("요청의 쿠키의 key값이 isRegistered인 쿠키가 없는 경우")
    void checkRegisteredGuestNull() {
        // given
        MockHttpServletRequest request = new MockHttpServletRequest();

        // when
        boolean checkBoardsId = cookieUtil.checkIsRegisteredGuest(request);
        // then
        assertThat(checkBoardsId).isFalse();
    }

    @ParameterizedTest
    @ValueSource(strings = {"null", "", " ", "false"})
    @DisplayName("요청의 쿠키의 key값이 isRegistered인 쿠키의 값이 true가 아닌 경우")
    void checkIsRegisteredGuestFalse(String value) {
        // given
        MockHttpServletRequest request = new MockHttpServletRequest();
        Cookie cookie = new Cookie("isRegistered", value);
        request.setCookies(cookie);

        // when
        boolean checkBoardsId = cookieUtil.checkIsRegisteredGuest(request);
        // then
        assertThat(checkBoardsId).isFalse();
    }

    @Test
    @DisplayName("요청의 쿠키의 key 값이 isRegistered인 쿠키의 값이 true인 경우")
    void checkIsRegisteredGuestTrue() {
        // given
        MockHttpServletRequest request = new MockHttpServletRequest();
        Cookie cookie = new Cookie("isRegistered", "true");
        request.setCookies(cookie);

        // when
        boolean checkBoardsId = cookieUtil.checkIsRegisteredGuest(request);
        // then
        assertThat(checkBoardsId).isTrue();
    }
    
    @Test
    @DisplayName("등록되지 않은 게스트라면 isRegistered 쿠키의 값을 true로 세팅해준다.")
    void setRegisteredGuestTrue() {
        // given
        MockHttpServletResponse response = new MockHttpServletResponse();
        // when
        cookieUtil.setRegisteredGuest(response);
        // then
        assertThat(response.getCookie("isRegistered").getValue()).isEqualTo("true");
    }
}