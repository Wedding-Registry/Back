package com.wedding.serviceapi.common.invitationinfo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface InvitationInfoSetter {

    boolean checkIsBoardsIdExist(HttpServletRequest request);

    void setBoardsId(HttpServletResponse response, String uuidFirst, String uuidSecond);

    boolean checkIsRegisteredGuest(HttpServletRequest request);

    void setRegisteredGuest(HttpServletResponse response);
}
