package com.wedding.serviceapi.guests.invitationinfo;

import com.wedding.serviceapi.exception.NoBoardsIdCookieExistException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface InvitationInfoSetter {

    default void checkInvitationInfoAndSettingInfoIfNotExist(HttpServletRequest request, HttpServletResponse response, Long usersId) {
        // boardsId 값이 있는지 확인
        if (!checkIsBoardsIdExist(request)) {
            throw new NoBoardsIdCookieExistException("어떤 게시판인지 알 수 없습니다. 게시판 정보를 보내주세요");
        }

        // isRegistered가 true인지 확인 후 없으면 세팅
        if (!checkIsRegisteredGuest(request)) {
            long boardsId = getBoardsId(request);
            saveGuest(usersId, boardsId);
            setRegisteredGuest(response);
        }
    }

    boolean checkIsBoardsIdExist(HttpServletRequest request);

    void setBoardsId(HttpServletRequest request, HttpServletResponse response, String uuidFirst, String uuidSecond);

    long getBoardsId(HttpServletRequest request);

    boolean checkIsRegisteredGuest(HttpServletRequest request);

    void setRegisteredGuest(HttpServletResponse response);

    void saveGuest(Long usersId, Long boardsId);
}
