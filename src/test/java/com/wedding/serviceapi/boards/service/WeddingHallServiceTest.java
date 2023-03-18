package com.wedding.serviceapi.boards.service;

import com.wedding.serviceapi.boards.domain.Boards;
import com.wedding.serviceapi.boards.dto.WeddingHallAddressDto;
import com.wedding.serviceapi.boards.dto.WeddingHallDateTimeDto;
import com.wedding.serviceapi.boards.repository.BoardsRepository;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@Slf4j
class WeddingHallServiceTest {

    @InjectMocks
    WeddingHallService weddingHallService;

    @Mock
    BoardsRepository boardsRepository;

    public Boards boards;
    public String address = "testAddress";

    @BeforeEach
    void setTest() {
        boards = new Boards();
    }

    @Test
    @DisplayName("결혼 주소 업데이트 성공")
    void updateWeddingHallAddressSuccess() {
        // given
        doReturn(Optional.of(boards)).when(boardsRepository).findById(anyLong());

        // when
        WeddingHallAddressDto result = weddingHallService.postWeddingHallAddress(anyLong(), address);

        // then
        assertThat(result.getAddress()).isEqualTo("testAddress");
    }

    @Test
    @DisplayName("결혼 게시판을 못찾는 경우 실패")
    void failToSearchBoards() {
        // given
        doReturn(Optional.empty()).when(boardsRepository).findById(anyLong());

        // when
        assertThrows(NoSuchElementException.class, () -> weddingHallService.postWeddingHallAddress(anyLong(), address));
    }

    @Test
    @DisplayName("결혼 날짜 및 시간 업데이트 성공")
    void updateWeddingHallDateTimeSuccess() {
        // given
        String date = "20231115";
        String time = "1500";
        doReturn(Optional.of(boards)).when(boardsRepository).findById(anyLong());

        // when
        WeddingHallDateTimeDto result = weddingHallService.postWeddingHallDateTime(anyLong(), date, time);

        // then
        assertThat(result.getWeddingDate()).isEqualTo("2023-11-15");
        assertThat(result.getWeddingTime()).isEqualTo("15:00");
    }
}





















