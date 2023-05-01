package com.wedding.serviceapi.goods.dto;

import com.wedding.serviceapi.boards.domain.Boards;
import lombok.Getter;

@Getter
public class MakeBoardResponseDto {
    private Long boardsId;
    private String uuidFirst;
    private String uuidSecond;
    private String accessToken;
    private String refreshToken;

    public MakeBoardResponseDto(Boards boards, String accessToken, String refreshToken) {
        this.boardsId = boards.getId();
        this.uuidFirst = boards.getUuidFirst();
        this.uuidSecond = boards.getUuidSecond();
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
