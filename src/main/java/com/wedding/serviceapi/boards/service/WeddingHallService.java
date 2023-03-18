package com.wedding.serviceapi.boards.service;

import com.wedding.serviceapi.boards.domain.Boards;
import com.wedding.serviceapi.boards.dto.WeddingHallAddressDto;
import com.wedding.serviceapi.boards.dto.WeddingHallDateTimeDto;
import com.wedding.serviceapi.boards.repository.BoardsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class WeddingHallService {

    private final BoardsRepository boardsRepository;

    public WeddingHallAddressDto postWeddingHallAddress(Long boardsId, String address) {
        Boards board = boardsRepository.findById(boardsId).orElseThrow(() -> new NoSuchElementException("해당하는 웨딩 게시판이 없습니다."));
        board.updateAddress(address);

        return new WeddingHallAddressDto(address);
    }

    public WeddingHallDateTimeDto postWeddingHallDateTime(Long boardsId, String date, String time) {
        Boards board = boardsRepository.findById(boardsId).orElseThrow(() -> new NoSuchElementException("해당하는 웨딩 게시판이 없습니다."));
        board.updateDateAndTime(date, time);

        return new WeddingHallDateTimeDto(board.getDate(), board.getTime());
    }

}




















