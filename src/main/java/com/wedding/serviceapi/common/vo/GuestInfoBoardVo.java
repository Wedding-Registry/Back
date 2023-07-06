package com.wedding.serviceapi.common.vo;

public class GuestInfoBoardVo {
    private Long boardsId;
    private Boolean isRegistered;

    public GuestInfoBoardVo(Long boardsId, Boolean isRegistered) {
        this.boardsId = boardsId;
        this.isRegistered = isRegistered;
    }
}
