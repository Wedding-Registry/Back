package com.wedding.serviceapi.boards.repository;

import com.wedding.serviceapi.boards.domain.Boards;
import com.wedding.serviceapi.users.domain.LoginType;
import com.wedding.serviceapi.users.domain.Users;
import com.wedding.serviceapi.users.repository.UsersRepository;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.AbstractThrowableAssert;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import javax.persistence.EntityManager;

import java.util.NoSuchElementException;
import java.util.Optional;

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

    public Boards board;
    public Boards savedBoard;
    public Users user;
    public Users savedUser;

    @BeforeEach
    void init() {
        user = Users.builder().name("test").password("password")
                .loginType(LoginType.KAKAO).build();

        savedUser = usersRepository.save(user);

        board = Boards.builder().id(1L).users(user).uuidFirst("first").uuidSecond("second").build();
        savedBoard = boardsRepository.save(board);
        em.flush();
        em.clear();
    }

    @Test
    @DisplayName("보드 아이디와 유저 아이디를 통해 보드 찾기")
    void findByIdAndUsersId() {
        // when
        Boards foundBoard = boardsRepository.findByIdAndUsersIdNotDeleted(savedBoard.getId(), savedUser.getId()).get();
        // then
        assertThat(foundBoard.getUsers().getName()).isEqualTo("test");
        assertThat(foundBoard.getUuidFirst()).isEqualTo("first");
        assertThat(foundBoard.getUuidSecond()).isEqualTo("second");
    }

    @Test
    @DisplayName("삭제하지 않은 보드를 가져오는지 확인")
    void checkExistedBoardDeletedNo() {
        // when
        Boards foundedBoard = boardsRepository.findByUsersIdNotDeleted(savedUser.getId()).get();
        // then
        assertThat(foundedBoard.getUuidFirst()).isEqualTo("first");
        assertThat(foundedBoard.getUuidSecond()).isEqualTo("second");
    }

    @Test
    @DisplayName("삭제한 보드를 가져오는지 확인")
    void checkExistedBoardDeletedYes() {
        // given
        Boards foundedBoard = boardsRepository.findByUsersIdNotDeleted(savedUser.getId()).get();
        foundedBoard.deleteEntity();
        em.flush();
        em.clear();
        // then
        assertThatThrownBy(() -> boardsRepository.findByUsersIdNotDeleted(savedUser.getId()).get())
                .isInstanceOf(NoSuchElementException.class);

    }
}


















