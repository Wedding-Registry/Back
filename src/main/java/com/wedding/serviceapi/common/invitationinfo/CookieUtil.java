package com.wedding.serviceapi.common.invitationinfo;

import com.wedding.serviceapi.boards.domain.Boards;
import com.wedding.serviceapi.boards.repository.BoardsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CookieUtil implements InvitationInfoSetter {

    private final BoardsRepository boardsRepository;

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
        response.addCookie(cookie);
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
    }
}
