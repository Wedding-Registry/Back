package com.wedding.serviceapi.common.vo;

import lombok.Getter;

@Getter
public class GuestBoardInfoVo {
    private Long boardsId;
    private Boolean isRegistered;

    public GuestBoardInfoVo(Long boardsId, Boolean isRegistered) {
        this.boardsId = boardsId;
        this.isRegistered = isRegistered;
    }
}
