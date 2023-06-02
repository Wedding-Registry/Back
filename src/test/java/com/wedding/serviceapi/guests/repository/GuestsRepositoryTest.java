package com.wedding.serviceapi.guests.repository;

import com.wedding.serviceapi.boards.domain.Boards;
import com.wedding.serviceapi.boards.repository.BoardsRepository;
import com.wedding.serviceapi.guests.domain.AttendanceType;
import com.wedding.serviceapi.guests.domain.Guests;
import com.wedding.serviceapi.users.domain.LoginType;
import com.wedding.serviceapi.users.domain.Users;
import com.wedding.serviceapi.users.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import javax.persistence.EntityManager;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Slf4j
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class GuestsRepositoryTest {

    private final GuestsRepository guestsRepository;
    private final UsersRepository usersRepository;
    private final BoardsRepository boardsRepository;
    private final EntityManager em;

    @Autowired
    public GuestsRepositoryTest(GuestsRepository guestsRepository, UsersRepository usersRepository, BoardsRepository boardsRepository, EntityManager em) {
        this.guestsRepository = guestsRepository;
        this.usersRepository = usersRepository;
        this.boardsRepository = boardsRepository;
        this.em = em;
    }

    private Users savedUser;
    private Users savedGuest;
    private Boards savedBoard;

    @BeforeEach
    void init() {
        Users user = Users.builder().email("user@test.com").password("user").name("user").loginType(LoginType.SERVICE).build();
        Users guest = Users.builder().email("guest@test.com").password("guest").name("guest").loginType(LoginType.SERVICE).build();
        Boards boards = Boards.builder().users(user).uuidFirst("uuid1").uuidSecond("uuid2").build();
        savedUser = usersRepository.save(user);
        savedGuest = usersRepository.save(guest);
        savedBoard = boardsRepository.save(boards);
        Guests newGuest = Guests.builder().users(savedGuest).boards(savedBoard).attendance(AttendanceType.YES).build();
        guestsRepository.save(newGuest);
        em.flush();
    }

    @Test
    @DisplayName("boardsId를 통해 guest 테이블에서 정보 가져오기")
    void findAllGuestsByBoardsId() {
        // given
        List<Guests> guestsList = guestsRepository.findAllByBoardsId(savedBoard.getId());
        // then
        Assertions.assertThat(guestsList.size()).isEqualTo(1);
    }

    @Test
    @DisplayName("boardsId를 통한 guest 가 한명도 없는 경우")
    void noGuests() {
        // given
        List<Guests> guestsList = guestsRepository.findAllByBoardsId(100L);
        // then
        Assertions.assertThat(guestsList.size()).isEqualTo(0);
    }

    @Test
    @DisplayName("boardsId를 통해 guest 테이블에서 유저 정보와 함께 데이터 가져오기")
    void findAllGuestsWithUsers() {
        // when
        List<Guests> guestsList = guestsRepository.findAllByBoardsIdWithUsers(savedBoard.getId());
        // then
        Assertions.assertThat(guestsList.size()).isEqualTo(1);
        Assertions.assertThat(guestsList.get(0).getUsers().getName()).isEqualTo("guest");
    }

    @Test
    @DisplayName("")
    void findByUsersIdAndBoardsId() {
        // when
        Guests guests = guestsRepository.findByUsersIdAndBoardsId(savedGuest.getId(), savedBoard.getId()).get();
        // then
        Assertions.assertThat(guests.getAttendance()).isEqualTo(AttendanceType.YES);
    }
}