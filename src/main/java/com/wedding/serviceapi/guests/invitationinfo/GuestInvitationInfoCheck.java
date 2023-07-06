package com.wedding.serviceapi.guests.invitationinfo;

import com.wedding.serviceapi.common.vo.GuestBoardInfoVo;

import javax.servlet.http.HttpServletRequest;

/**
 * 1. jwt 토큰에 게스트 정보가 있는지 확인
 * 2. jwt 토큰을 해석해서 GuestInfoBoardVo를 반환한다.
 * 3. jwt 토큰을 생성해준다.
 */
public interface GuestInvitationInfoCheck {

    String getJwt(HttpServletRequest request);

    GuestBoardInfoVo getGuestBoardInfo(HttpServletRequest request);

    String makeGuestBoardInfoJwt(Long boardsId, Long usersId);
}
