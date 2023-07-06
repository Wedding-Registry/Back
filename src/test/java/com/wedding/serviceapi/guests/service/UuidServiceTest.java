package com.wedding.serviceapi.guests.service;

import com.wedding.serviceapi.boards.domain.Boards;
import com.wedding.serviceapi.boards.repository.BoardsRepository;
import com.wedding.serviceapi.guests.dto.GuestInfoJwtDto;
import com.wedding.serviceapi.guests.dto.UuidResponseDto;
import com.wedding.serviceapi.guests.invitationinfo.HeaderUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@Slf4j
class UuidServiceTest {

    @InjectMocks
    UuidService uuidService;

    @Mock
    private BoardsRepository boardsRepository;
    @Mock
    private HeaderUtil headerUtil;

    @Test
    @DisplayName("uuidResponseDto 정상 생성 테스트")
    void makeUuidResponseDto() {
        // given
        Boards boards = Boards.builder().id(1L).uuidFirst("first").uuidSecond("second").build();
        doReturn(Optional.of(boards)).when(boardsRepository).findByIdAndUsersIdNotDeleted(1L, 1L);
        // when
        UuidResponseDto result = uuidService.getUuid(1L, 1L);
        // then
        assertThat(result.getUuidFirst()).isEqualTo("first");
        assertThat(result.getUuidSecond()).isEqualTo("second");
    }

    @Test
    @DisplayName("uuidFirst 와 uuidSecond 에 해당하는 결혼 게시판이 없는 경우 에러를 발생시킨다.")
    void noBoards() {
        // given
        doReturn(Optional.empty()).when(boardsRepository).findByUuidFirstAndUuidSecond(anyString(), anyString());
        // when
        // then
        assertThatThrownBy(() -> uuidService.makeGuestInfoJwt(anyString(), anyString(), 1L))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessage("해당하는 결혼 게시판이 없습니다.");
    }

    @Test
    @DisplayName("uuidFirst 와 uuidSecond 에 해당하는 결혼 게시판이 있는경우 해당 정보를 담은 jwt 토큰을 반환한다.")
    void makeJwt() {
        // given
        Boards boards = Boards.builder().id(1L).build();
        doReturn(Optional.of(boards)).when(boardsRepository).findByUuidFirstAndUuidSecond(anyString(), anyString());
        doReturn("testJwt").when(headerUtil).makeGuestBoardInfoJwt(1L, 1L);
        // when
        GuestInfoJwtDto guestInfoJwtDto = uuidService.makeGuestInfoJwt(anyString(), anyString(), 1L);
        // then
        assertThat(guestInfoJwtDto.getGuestInfoJwt()).isEqualTo("testJwt");
    }

}