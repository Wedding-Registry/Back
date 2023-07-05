package com.wedding.serviceapi.guests.service;

import com.wedding.serviceapi.boards.domain.Boards;
import com.wedding.serviceapi.boards.repository.BoardsRepository;
import com.wedding.serviceapi.guests.dto.UuidResponseDto;
import com.wedding.serviceapi.guests.invitationinfo.InvitationInfoSetter;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.mock.http.client.MockClientHttpResponse;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@Slf4j
class UuidServiceTest {

    @InjectMocks
    UuidService uuidService;

    @Mock
    private BoardsRepository boardsRepository;

    @Mock
    private InvitationInfoSetter invitationInfoSetter;

    @Disabled
    @Test
    @DisplayName("uuidResponseDto 정상 생성 테스트")
    void makeUuidResponseDto() {
        // given
        Boards boards = Boards.builder().id(1L).uuidFirst("first").uuidSecond("second").build();
        doReturn(Optional.of(boards)).when(boardsRepository).findByIdAndUsersIdNotDeleted(1L, 1L);
        // when
        UuidResponseDto result = uuidService.getUuid(1L, 1L);
        // then
//        assertThat(result.getBoardsId()).isEqualTo(1L);
        assertThat(result.getUuidFirst()).isEqualTo("first");
        assertThat(result.getUuidSecond()).isEqualTo("second");
    }

    @Disabled
    @Test
    @DisplayName("cookie에 boardsId 쿠키 값을 정상적으로 세팅해준다.")
    void setBoardsIdCookie() {
        // given
        MockHttpServletResponse response = new MockHttpServletResponse();
        doAnswer(invocation -> {
            HttpServletResponse res = invocation.getArgument(0);
            res.addCookie(new Cookie("boardsId", "1"));
            return null;
        }).when(invitationInfoSetter).setBoardsId(response, "first", "second");
        // when
        uuidService.setBoardsIdCookie("first", "second", response);
        // then
        assertThat(response.getCookie("boardsId").getValue()).isEqualTo("1");
    }

}