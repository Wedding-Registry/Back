package com.wedding.serviceapi.guests.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UuidRequestDto {
    private String uuidFirst;
    private String uuidSecond;

    @Builder
    private UuidRequestDto(String uuidFirst, String uuidSecond) {
        this.uuidFirst = uuidFirst;
        this.uuidSecond = uuidSecond;
    }
}
