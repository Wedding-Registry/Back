package com.wedding.serviceapi.goods.dto;

import com.wedding.serviceapi.boards.domain.Boards;
import lombok.Getter;

@Getter
public class MakeBoardResponseDto {
    private Long boardsId;
    private String uuidFirst;
    private String uuidSecond;

    public MakeBoardResponseDto(Boards boards) {
        this.boardsId = boards.getId();
        this.uuidFirst = boards.getUuidFirst();
        this.uuidSecond = boards.getUuidSecond();
    }
}
