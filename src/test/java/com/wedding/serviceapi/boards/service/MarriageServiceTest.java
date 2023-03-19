package com.wedding.serviceapi.boards.service;

import com.wedding.serviceapi.boards.domain.Boards;
import com.wedding.serviceapi.boards.domain.HusbandAndWifeEachInfo;
import com.wedding.serviceapi.boards.dto.marriage.MarriageBankAccountDto;
import com.wedding.serviceapi.boards.dto.marriage.MarriageNameDto;
import com.wedding.serviceapi.boards.repository.BoardsRepository;
import com.wedding.serviceapi.exception.NoSuchPathTypeException;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@Slf4j
class MarriageServiceTest {

    @InjectMocks
    MarriageService marriageService;

    @Mock
    BoardsRepository boardsRepository;

    @Test
    @DisplayName("신랑 신부 이름 수정 테스트")
    void changeHusbandOrWifeName() {
        // given
        String type = "husband";
        HusbandAndWifeEachInfo husband = new HusbandAndWifeEachInfo();
        HusbandAndWifeEachInfo wife = new HusbandAndWifeEachInfo();
        Boards boards = Boards.builder().husband(husband).wife(wife).build();
        String name = "test";

        doReturn(Optional.of(boards)).when(boardsRepository).findById(anyLong());

        // when
        MarriageNameDto data = marriageService.postHusbandOrWifeName(type, anyLong(), name);

        // then
        assertThat(data.getName()).isEqualTo("test");
        assertThat(boards.getHusband().getName()).isEqualTo("test");
        assertThat(boards.getHusband().getBank()).isNull();
        assertThat(boards.getWife().getName()).isNull();
    }

    @Test
    @DisplayName("신랑 신부 이름 수정 실패 테스트")
    void changeHusbandOrWifeNameFail() {
        // given
        String type = "noType";
        HusbandAndWifeEachInfo husband = new HusbandAndWifeEachInfo();
        HusbandAndWifeEachInfo wife = new HusbandAndWifeEachInfo();
        Boards boards = Boards.builder().husband(husband).wife(wife).build();
        String name = "test";

        doReturn(Optional.of(boards)).when(boardsRepository).findById(anyLong());

        // when
        assertThrows(NoSuchPathTypeException.class, () -> marriageService.postHusbandOrWifeName(type, anyLong(), name));
    }

    @Test
    @DisplayName("신랑 신부 은행 계좌 수정 성공")
    void changeHusbandOrWifeBankAccount() {
        // given
        String type = "wife";
        HusbandAndWifeEachInfo husband = new HusbandAndWifeEachInfo();
        HusbandAndWifeEachInfo wife = new HusbandAndWifeEachInfo();
        Boards boards = Boards.builder().husband(husband).wife(wife).build();
        String bank = "bank test";
        String account = "account test";

        doReturn(Optional.of(boards)).when(boardsRepository).findById(anyLong());

        // when
        MarriageBankAccountDto data = marriageService.postMarriageBankAndAccount(type, anyLong(), bank, account);

        // then
        assertThat(data.getBank()).isEqualTo("bank test");
        assertThat(data.getAccount()).isEqualTo("account test");
    }
}

















