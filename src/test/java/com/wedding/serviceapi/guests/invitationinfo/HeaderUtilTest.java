package com.wedding.serviceapi.guests.invitationinfo;

import com.wedding.serviceapi.boards.domain.Boards;
import com.wedding.serviceapi.boards.repository.BoardsRepository;
import com.wedding.serviceapi.common.jwtutil.JwtUtilBean;
import com.wedding.serviceapi.common.vo.GuestBoardInfoVo;
import com.wedding.serviceapi.exception.NoGuestBoardsInfoJwtExistException;
import com.wedding.serviceapi.users.domain.Users;
import com.wedding.serviceapi.users.repository.UsersRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
class HeaderUtilTest {

    @Autowired
    private HeaderUtil headerUtil;
    @Autowired
    private JwtUtilBean<GuestBoardInfoVo> jwtUtil;
    @Autowired
    UsersRepository usersRepository;
    @Autowired
    BoardsRepository boardsRepository;

    @Test
    @DisplayName("header에 Guest-Info 헤더가 없는 경우 토큰이 없다는 에러 발생")
    void noGuestInfoHeader() {
        // given
        MockHttpServletRequest request = new MockHttpServletRequest();
        // when
        // then
        assertThatThrownBy(() -> headerUtil.getJwt(request))
                .isInstanceOf(NoGuestBoardsInfoJwtExistException.class)
                .hasMessage("Guest-Info 헤더값이 없습니다.");
    }

    @Test
    @DisplayName("header에 Guest-Info 헤더가 있는 경우 해당 헤더의 값인 jwt 토큰을 반환한다.")
    void withGuestInfoHeader() {
        // given
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Guest-Info", "testJwt");
        // when
        String jwt = headerUtil.getJwt(request);
        // then
        assertThat(jwt).isEqualTo("testJwt");
    }

    @Test
    @DisplayName("jwt 토큰에서 정보를 가져올 때 해당하는 정보가 없는 경우 에러를 발생한다")
    void notCorrectInfoToken() {
        // given
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Guest-Info", "testJwt");
        // when
        // then
        assertThatThrownBy(() -> headerUtil.getGuestBoardInfo(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("잘못된 토큰 값입니다.");
    }

    @Test
    @DisplayName("jwt 토큰에서 정상적으로 정보를 가져오는 경우 boardsId 값과 isRegistered 값이 존재한다.")
    void getGuestBoardInfo() {
        // given
        Users users = makeUser("user");
        Users guest = makeUser("guest");
        Boards boards = makeBoard(users);
        String jwt = jwtUtil.makeGuestInfoJwt(boards.getId(), guest.getId());

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Guest-Info", jwt);
        // when
        GuestBoardInfoVo guestBoardInfo = headerUtil.getGuestBoardInfo(request);
        // then
        assertThat(guestBoardInfo.getBoardsId()).isEqualTo(boards.getId());
        assertThat(guestBoardInfo.getIsRegistered()).isTrue();
    }

    @Test
    @DisplayName("게시판 아이디와 결혼식 초대를 받은 사용자의 아이디를 받아 jwt 토큰을 만들어준다.")
    void makeJwtByBoardsIdAndUserId() {
        // given
        Users users = makeUser("user");
        Users guest = makeUser("guest");
        Boards boards = makeBoard(users);
        // when
        String jwt = headerUtil.makeGuestBoardInfoJwt(boards.getId(), guest.getId());
        // then
        assertThat(jwt).isNotNull();
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