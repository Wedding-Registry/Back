package com.wedding.serviceapi.guests.invitationinfo;

import com.wedding.serviceapi.boards.domain.Boards;
import com.wedding.serviceapi.boards.repository.BoardsRepository;
import com.wedding.serviceapi.exception.NoBoardsIdCookieExistException;
import com.wedding.serviceapi.guests.domain.Guests;
import com.wedding.serviceapi.guests.repository.GuestsRepository;
import com.wedding.serviceapi.users.domain.Users;
import com.wedding.serviceapi.users.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class CookieUtil implements InvitationInfoSetter {

    private final BoardsRepository boardsRepository;
    private final UsersRepository usersRepository;
    private final GuestsRepository guestsRepository;

    private final String BOARDS_ID = "boardsId";
    private final String IS_REGISTERED = "isRegistered";

    @Override
    public boolean checkIsBoardsIdExist(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null || cookies.length == 0) {
            return false;
        }
        return Arrays.stream(cookies)
                .filter(cookie -> cookie.getName().equals(BOARDS_ID)).findFirst()
                .map(cookie -> isLongValue(cookie.getValue())).orElse(false);
    }

    private boolean isLongValue(String value) {
        try {
            Long.parseLong(value);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * uuid1, uuid2 를 이용해 boardsId를 찾아서 쿠키에 저장한다.
     */
    @Override
    public void setBoardsId(HttpServletResponse response, String uuidFirst, String uuidSecond) {
        Boards boards = boardsRepository.findByUuidFirstAndUuidSecond(uuidFirst, uuidSecond)
                .orElseThrow(() -> new NoSuchElementException("해당하는 웨딩 게시판이 없습니다."));
        Cookie cookie = new Cookie(BOARDS_ID, String.valueOf(boards.getId()));
        cookie.setSecure(true);
        cookie.setHttpOnly(true);
        cookie.setMaxAge(86400);
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    @Override
    public long getBoardsId(HttpServletRequest request) {
        String boardsId = Arrays.stream(request.getCookies())
                .filter(cookie -> cookie.getName().equals(BOARDS_ID))
                .map(Cookie::getValue)
                .findFirst().orElseThrow(() -> new NoBoardsIdCookieExistException("해당하는 boardsId값이 없습니다."));
        return Long.parseLong(boardsId);
    }

    @Override
    public boolean checkIsRegisteredGuest(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null || cookies.length == 0) {
            return false;
        }
        return Arrays.stream(cookies)
                .filter(cookie -> cookie.getName().equals(IS_REGISTERED)).findFirst()
                .map(cookie -> cookie.getValue().equals("true")).orElse(false);
    }

    @Override
    public void setRegisteredGuest(HttpServletResponse response) {
        Cookie cookie = new Cookie(IS_REGISTERED, String.valueOf(true));
        response.addCookie(cookie);
        // TODO: 2023/07/04 http only 쿠키 등 추가 설정 필요
    }

    public void saveGuest(Long usersId, Long boardsId) {
        boolean isNewInvitedUser = guestsRepository.findByUsersIdAndBoardsId(usersId, boardsId).isEmpty();
        if (isNewInvitedUser) {
            Users invitedUser = usersRepository.getReferenceById(usersId);
            Boards board = boardsRepository.getReferenceById(boardsId);
            Guests guest = Guests.builder()
                    .boards(board)
                    .users(invitedUser)
                    .build();
            guestsRepository.save(guest);
        }
    }
}
