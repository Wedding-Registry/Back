package com.wedding.serviceapi.common.cookieutil;

import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

public class CookieUtil implements InvitationInfoSetter {

    private final Cookie[] cookies;
    private final HttpServletResponse response;

    public CookieUtil(Cookie[] cookies, HttpServletResponse response) {
        this.cookies = cookies;
        this.response = response;
    }

    @Override
    public boolean checkBoardsId() {
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("boardsId") && !cookie.getName().isBlank()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void setBoardsId() {

    }

    @Override
    public boolean checkIsRegisteredGuest() {
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("isRegisteredGuest") && cookie.getValue().equals("true")) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void setRegisteredGuest() {

    }
}
