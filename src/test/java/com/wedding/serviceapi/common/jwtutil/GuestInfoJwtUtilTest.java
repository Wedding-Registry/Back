package com.wedding.serviceapi.common.jwtutil;

import com.wedding.serviceapi.boards.domain.Boards;
import com.wedding.serviceapi.boards.repository.BoardsRepository;
import com.wedding.serviceapi.common.vo.GuestBoardInfoVo;
import com.wedding.serviceapi.guests.domain.Guests;
import com.wedding.serviceapi.guests.repository.GuestsRepository;
import com.wedding.serviceapi.users.domain.Users;
import com.wedding.serviceapi.users.repository.UsersRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
class GuestInfoJwtUtilTest {

    @Autowired
    JwtUtilBean<GuestBoardInfoVo> jwtUtil;
    @Autowired
    UsersRepository usersRepository;
    @Autowired
    BoardsRepository boardsRepository;
    @Autowired
    GuestsRepository guestsRepository;

    String jwt;

    @DisplayName("guest 테이블에 등록되지 않은 사용자라면 테이블에 등록해주고 jwt를 발급한다.")
    @Test
    void registerNewGuest() {
        // given
        Users users = makeUser("user");
        Users guest = makeUser("guest");
        Boards boards = makeBoard(users);
        // when
        String jwt = jwtUtil.makeGuestInfoJwt(boards.getId(), guest.getId());
        // then
        assertThat(jwt).isNotEmpty();
        Optional<Guests> optional = guestsRepository.findByUsersIdAndBoardsId(guest.getId(), boards.getId());
        assertThat(optional.isPresent()).isTrue();
    }

    @DisplayName("올바르지 않은 토큰이 들어왔을 때 잘못된 토큰이라는 메시지를 발행한다.")
    @Test
    void illegalToken() {
        // given
        String jwt = "testToken";
        // when
        // then
        assertThatThrownBy(() -> jwtUtil.decodeJwt(jwt))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("잘못된 토큰 값입니다.");
    }

    @TestFactory
    Collection<DynamicTest> guestInfoJwtDynamicTest() {
//        AtomicReference<String> jwt;
        return List.of(
                DynamicTest.dynamicTest("올바른 요청이라면 jwt 토큰을 발급해준다.", () -> {
                    // given
                    Users users = makeUser("user");
                    Users guest = makeUser("guest");
                    Boards boards = makeBoard(users);
                    // when
                    jwt = jwtUtil.makeGuestInfoJwt(boards.getId(), guest.getId());
                    // then
                    assertThat(jwt).isNotEmpty();
                    Optional<Guests> optional = guestsRepository.findByUsersIdAndBoardsId(guest.getId(), boards.getId());
                    assertThat(optional.isPresent()).isTrue();
                }),
                DynamicTest.dynamicTest("jwt를 전달받으면 정상적으로 토큰을 해석해준다.", () -> {
                    GuestBoardInfoVo guestInfoBoardVo = jwtUtil.decodeJwt(jwt);
                    assertThat(guestInfoBoardVo.getBoardsId()).isNotNull();
                    assertThat(guestInfoBoardVo.getIsRegistered()).isTrue();
                })
        );
    }



    private Users makeUser(String userName) {
        Users user = Users.builder()
                .name(userName)
                .build();

        return usersRepository.save(user);
    }

    private Boards makeBoard(Users users) {
        Boards boards = Boards.builder()
                .uuidFirst("first")
                .uuidSecond("second")
                .users(users)
                .build();

        return boardsRepository.save(boards);
    }

}