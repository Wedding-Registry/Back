package com.wedding.serviceapi.guests.dto;

import com.wedding.serviceapi.boards.domain.Boards;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UuidResponseDto {
    private Long boardsId;
    private String uuidFirst;
    private String uuidSecond;

    public static UuidResponseDto from(Boards boards) {
        return new UuidResponseDto(boards.getId(), boards.getUuidFirst(), boards.getUuidSecond());
    }
}
