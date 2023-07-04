package com.wedding.serviceapi.guests.service;

import com.wedding.serviceapi.boards.domain.Boards;
import com.wedding.serviceapi.boards.repository.BoardsRepository;
import com.wedding.serviceapi.guests.dto.UuidResponseDto;
import com.wedding.serviceapi.guests.invitationinfo.InvitationInfoSetter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class UuidService {

    private final BoardsRepository boardsRepository;
    private final InvitationInfoSetter invitationInfoSetter;

    public UuidResponseDto getUuid(Long boardsId, Long usersId) {
        Boards boards = boardsRepository.findByIdAndUsersIdNotDeleted(boardsId, usersId)
                .orElseThrow(() -> new NoSuchElementException("해당하는 결혼 게시판이 없습니다."));
        return UuidResponseDto.from(boards);
    }

    public void setBoardsIdCookie(String uuidFirst, String uuidSecond, HttpServletResponse response) {
        invitationInfoSetter.setBoardsId(response, uuidFirst, uuidSecond);
    }
}
