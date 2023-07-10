package com.wedding.serviceapi.boards.service;

import com.wedding.serviceapi.boards.domain.Boards;
import com.wedding.serviceapi.boards.domain.HusbandAndWifeEachInfo;
import com.wedding.serviceapi.boards.dto.weddinghall.*;
import com.wedding.serviceapi.boards.repository.BoardsRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
    @DisplayName("결혼 게시판 관련 정보 불러오기 성공")
    void getWeddingHallInfo() {
        // given
        HusbandAndWifeEachInfo husband = new HusbandAndWifeEachInfo("husband name", "husband bank", "husband account");
        HusbandAndWifeEachInfo wife = new HusbandAndWifeEachInfo("wife name", "wife bank", "wife account");
        Boards board = Boards.builder().husband(husband).wife(wife).address("test address").date("2023-02-16").time("15:50").build();

        doReturn(Optional.of(board)).when(boardsRepository).findByIdAndUsersIdNotDeleted(anyLong(), anyLong());

        // when
        WeddingHallInfoDto data = weddingHallService.getWeddingHallInfo(anyLong(), anyLong());

        // then
        assertAll(
                () -> assertThat(data.getAccount().size()).isEqualTo(2),
                () -> assertThat(data.getLocation()).isEqualTo("test address"),
                () -> assertThat(data.getWeddingDate()).isEqualTo("2023-02-16"),
                () -> assertThat(data.getWeddingTime()).isEqualTo("15:50"),
                () -> assertThat(data.getAccount().get(0).getBank()).isEqualTo("husband bank"),
                () -> assertThat(data.getAccount().get(1).getBank()).isEqualTo("wife bank")
        );
    }



    @Test
    @DisplayName("결혼 주소 업데이트 성공")
    void updateWeddingHallAddressSuccess() {
        // given
        doReturn(Optional.of(boards)).when(boardsRepository).findByIdAndUsersIdNotDeleted(anyLong(), anyLong());

        // when
        WeddingHallAddressDto result = weddingHallService.postWeddingHallAddress(anyLong(), address, anyLong());

        // then
        assertThat(result.getAddress()).isEqualTo("testAddress");
    }

    @Test
    @DisplayName("결혼 게시판을 못찾는 경우 실패")
    void failToSearchBoards() {
        // given
        doReturn(Optional.empty()).when(boardsRepository).findByIdAndUsersIdNotDeleted(anyLong(), anyLong());

        // when
        assertThrows(NoSuchElementException.class, () -> weddingHallService.postWeddingHallAddress(anyLong(), address, anyLong()));
    }

    @Test
    @DisplayName("결혼 날짜 및 시간 업데이트 성공")
    void updateWeddingHallDateTimeSuccess() {
        // given
        String date = "20231115";
        String time = "1500";
        doReturn(Optional.of(boards)).when(boardsRepository).findByIdAndUsersIdNotDeleted(anyLong(), anyLong());

        // when
        WeddingHallDateTimeDto result = weddingHallService.postWeddingHallDateTime(anyLong(), date, time, anyLong());

        // then
        assertThat(result.getWeddingDate()).isEqualTo("2023-11-15");
        assertThat(result.getWeddingTime()).isEqualTo("15:00");
    }
}





















