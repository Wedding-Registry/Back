package com.wedding.serviceapi.admin.dto.memo;

import com.wedding.serviceapi.boards.domain.Boards;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@Getter
@NoArgsConstructor
public class PadContentsDto {
    @NotBlank(message = "메모를 입력해주세요")
    private String contents;

    public static PadContentsDto from(Boards boards) {
        return new PadContentsDto(boards.getBoardsMemo());
    }
}
