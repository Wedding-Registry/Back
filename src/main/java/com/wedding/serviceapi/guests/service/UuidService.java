package com.wedding.serviceapi.guests.service;

import com.wedding.serviceapi.boards.domain.Boards;
import com.wedding.serviceapi.boards.repository.BoardsRepository;
import com.wedding.serviceapi.guests.dto.UuidResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class UuidService {

    private final BoardsRepository boardsRepository;

    public UuidResponseDto getUuid(Long boardsId, Long usersId) {
        Boards boards = boardsRepository.findByIdAndUsersIdNotDeleted(boardsId, usersId)
                .orElseThrow(() -> new NoSuchElementException("해당하는 결혼 게시판이 없습니다."));
        return UuidResponseDto.from(boards);
    }
}
