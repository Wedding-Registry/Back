package com.wedding.serviceapi.guests.service;

import com.wedding.serviceapi.boards.domain.Boards;
import com.wedding.serviceapi.boards.repository.BoardsRepository;
import com.wedding.serviceapi.guests.dto.UuidResponseDto;
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
class UuidServiceTest {

    @InjectMocks
    UuidService uuidService;

    @Mock
    BoardsRepository boardsRepository;

    @Test
    @DisplayName("uuidResponseDto 정상 생성 테스트")
    void makeUuidResponseDto() {
        // given
        Boards boards = Boards.builder().id(1L).uuidFirst("first").uuidSecond("second").build();
        doReturn(Optional.of(boards)).when(boardsRepository).findByIdAndUsersIdNotDeleted(anyLong(), anyLong());
        // when
        UuidResponseDto result = uuidService.getUuid(anyLong(), anyLong());
        // then
        assertThat(result.getBoardsId()).isEqualTo(1L);
        assertThat(result.getUuidFirst()).isEqualTo("first");
        assertThat(result.getUuidSecond()).isEqualTo("second");
    }

}