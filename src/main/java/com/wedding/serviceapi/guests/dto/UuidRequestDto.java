package com.wedding.serviceapi.guests.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UuidRequestDto {
    @NotBlank(message = "uuidFirst 값은 필수입니다.")
    private String uuidFirst;
    @NotBlank(message = "uuidSecond 값은 필수입니다.")
    private String uuidSecond;

    @Builder
    private UuidRequestDto(String uuidFirst, String uuidSecond) {
        this.uuidFirst = uuidFirst;
        this.uuidSecond = uuidSecond;
    }
}
