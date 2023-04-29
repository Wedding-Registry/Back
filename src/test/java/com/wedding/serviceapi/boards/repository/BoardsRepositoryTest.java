package com.wedding.serviceapi.boards.repository;

import com.wedding.serviceapi.boards.domain.Boards;
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

import javax.persistence.EntityManager;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Slf4j
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class BoardsRepositoryTest {

    @Autowired
    BoardsRepository boardsRepository;
    @Autowired
    UsersRepository usersRepository;

    @Autowired
    EntityManager em;

    @Test
    @DisplayName("보드 아이디와 유저 아이디를 통해 보드 찾기")
    void findByIdAndUsersId() {
        // given
        Users user = Users.builder().name("test").password("password")
                .loginType(LoginType.KAKAO).build();

        Users savedUser = usersRepository.save(user);

        Boards board = Boards.builder().id(1L).users(user).uuidFirst("first").uuidSecond("second").build();
        Boards savedBoard = boardsRepository.save(board);
        em.flush();
        em.clear();
        // when

        log.info("savedUser = {} savedBoard = {}", savedUser.getId(), savedBoard.getId());

        Boards foundBoard = boardsRepository.findByIdAndUsersId(savedBoard.getId(), savedUser.getId()).get();
        // then
        assertThat(foundBoard.getUsers().getName()).isEqualTo("test");
        assertThat(foundBoard.getUuidFirst()).isEqualTo("first");
        assertThat(foundBoard.getUuidSecond()).isEqualTo("second");
    }
}


















