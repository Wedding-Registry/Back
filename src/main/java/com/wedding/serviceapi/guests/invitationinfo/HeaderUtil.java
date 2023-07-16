package com.wedding.serviceapi.guests.invitationinfo;

import com.wedding.serviceapi.common.jwtutil.GuestInfoJwtUtil;
import com.wedding.serviceapi.common.vo.GuestBoardInfoVo;
import com.wedding.serviceapi.exception.NoGuestBoardsInfoJwtExistException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Service
@RequiredArgsConstructor
public class HeaderUtil implements GuestInvitationInfoCheck {

    private final GuestInfoJwtUtil jwtUtil;

    private final String GUEST_INFO_HEADER_KEY = "Guest-Info";

    @Override
    public String getJwt(HttpServletRequest request) {
        String guestInfoJwt = request.getHeader(GUEST_INFO_HEADER_KEY);
        if (guestInfoJwt == null) {
            throw new NoGuestBoardsInfoJwtExistException("Guest-Info 헤더값이 없습니다.");
        }
        return guestInfoJwt;
    }

    @Override
    public GuestBoardInfoVo getGuestBoardInfo(HttpServletRequest request) {
        String jwt = getJwt(request);
        return jwtUtil.decodeJwt(jwt);
    }

    @Override
    public String makeGuestBoardInfoJwt(Long boardsId, Long usersId) {
        return jwtUtil.makeGuestInfoJwt(boardsId, usersId);
    }
}
