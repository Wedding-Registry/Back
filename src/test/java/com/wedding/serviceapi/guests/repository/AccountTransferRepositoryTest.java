package com.wedding.serviceapi.guests.repository;

import com.wedding.serviceapi.boards.domain.Boards;
import com.wedding.serviceapi.boards.repository.BoardsRepository;
import com.wedding.serviceapi.guests.domain.AccountTransfer;
import com.wedding.serviceapi.users.domain.LoginType;
import com.wedding.serviceapi.users.domain.Users;
import com.wedding.serviceapi.users.repository.UsersRepository;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Slf4j
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class AccountTransferRepositoryTest {

    @Autowired
    AccountTransferRepository accountTransferRepository;
    @Autowired
    UsersRepository usersRepository;
    @Autowired
    BoardsRepository boardsRepository;

    @Test
    @DisplayName("boardsId를 통해 모든 계좌이체 정보 가져오기")
    void getAllByBoardsIdTest() {
        // given
        Users users = Users.builder().name("test").password("password").email("test").loginType(LoginType.KAKAO).build();
        Users savedUsers = usersRepository.saveAndFlush(users);
        Boards boards = Boards.builder().users(savedUsers).uuidFirst("first").uuidSecond("second").build();
        Boards savedBoards = boardsRepository.saveAndFlush(boards);
        AccountTransfer accountTransfer1 = AccountTransfer.builder().boards(savedBoards).transferMemo("test1 memo").build();
        AccountTransfer accountTransfer2 = AccountTransfer.builder().boards(savedBoards).transferMemo("test2 memo").build();
        accountTransferRepository.saveAllAndFlush(List.of(accountTransfer1, accountTransfer2));

        // when
        List<AccountTransfer> transferList = accountTransferRepository.findAllByBoardsId(savedBoards.getId());
        // then
        assertThat(transferList.size()).isEqualTo(2);
        assertThat(transferList.get(0).getTransferMemo()).isEqualTo("test2 memo");
        assertThat(transferList.get(1).getTransferMemo()).isEqualTo("test1 memo");
    }
}