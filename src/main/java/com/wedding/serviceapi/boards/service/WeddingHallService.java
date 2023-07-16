package com.wedding.serviceapi.boards.service;

import com.wedding.serviceapi.boards.domain.Boards;
import com.wedding.serviceapi.boards.dto.weddinghall.WeddingHallAddressDto;
import com.wedding.serviceapi.boards.dto.weddinghall.WeddingHallDateTimeDto;
import com.wedding.serviceapi.boards.dto.weddinghall.WeddingHallInfoDto;
import com.wedding.serviceapi.boards.repository.BoardsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class WeddingHallService {

    private final BoardsRepository boardsRepository;

    @Transactional(readOnly = true)
    public WeddingHallInfoDto getWeddingHallInfo(Long boardsId, Long userId) {
        Boards board = boardsRepository.findByIdAndUsersIdNotDeleted(boardsId, userId).orElseThrow(() -> new NoSuchElementException("해당하는 웨딩 게시판이 없습니다."));
        return new WeddingHallInfoDto(board);
    }

    @Transactional(readOnly = true)
    public WeddingHallInfoDto getWeddingHallInfo(Long boardsId) {
        Boards board = boardsRepository.findById(boardsId).orElseThrow(() -> new NoSuchElementException("해당하는 웨딩 게시판이 없습니다."));
        return new WeddingHallInfoDto(board);
    }


    public WeddingHallAddressDto postWeddingHallAddress(Long boardsId, String address, Long userId) {
        Boards board = boardsRepository.findByIdAndUsersIdNotDeleted(boardsId, userId).orElseThrow(() -> new NoSuchElementException("해당하는 웨딩 게시판이 없습니다."));
        board.updateAddress(address);

        return new WeddingHallAddressDto(address);
    }

    public WeddingHallDateTimeDto postWeddingHallDateTime(Long boardsId, String date, String time, Long userId) {
        Boards board = boardsRepository.findByIdAndUsersIdNotDeleted(boardsId, userId).orElseThrow(() -> new NoSuchElementException("해당하는 웨딩 게시판이 없습니다."));
        board.updateDateAndTime(date, time);

        return new WeddingHallDateTimeDto(board.getDate(), board.getTime());
    }

}




















