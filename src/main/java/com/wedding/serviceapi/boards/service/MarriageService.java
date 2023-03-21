package com.wedding.serviceapi.boards.service;

import com.wedding.serviceapi.boards.domain.Boards;
import com.wedding.serviceapi.boards.dto.marriage.MarriageBankAccountDto;
import com.wedding.serviceapi.boards.dto.marriage.MarriageNameDto;
import com.wedding.serviceapi.boards.repository.BoardsRepository;
import com.wedding.serviceapi.exception.NoSuchPathTypeException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class MarriageService {

    private final BoardsRepository boardsRepository;

    private static String HUSBAND_TYPE = "husband";
    private static String WIFE_TYPE = "wife";

    public MarriageNameDto postHusbandOrWifeName(String type, Long boardsId, String name) {
        // TODO: 2023/03/19 사용자 자신의 게시판인지 결혼 상대의 게시판인지 확인 필요
        Boards boards = boardsRepository.findById(boardsId).orElseThrow(() -> new NoSuchElementException("해당하는 게시판이 없습니다."));

        if (type.equals(HUSBAND_TYPE)) {
            boards.getHusband().changeName(name);
        } else if (type.equals(WIFE_TYPE)) {
            boards.getWife().changeName(name);
        } else {
            throw new NoSuchPathTypeException("잘못된 url 정보 입니다.");
        }
        return new MarriageNameDto(name);
    }

    public MarriageBankAccountDto postMarriageBankAndAccount(String type, Long boardsId, String bank, String account) {
        // TODO: 2023/03/19 사용자 자신의 게시판인지 결혼 상대의 게시판인지 확인 필요
        Boards boards = boardsRepository.findById(boardsId).orElseThrow(() -> new NoSuchElementException("해당하는 게시판이 없습니다."));
        if (type.equals(HUSBAND_TYPE)) {
            boards.getHusband().changeBank(bank);
            boards.getHusband().changeAccount(account);
        } else if (type.equals(WIFE_TYPE)) {
            boards.getWife().changeBank(bank);
            boards.getWife().changeAccount(account);
        } else {
            throw new NoSuchPathTypeException("잘못된 url 정보 입니다.");
        }

        return new MarriageBankAccountDto(bank, account);
    }

}



















