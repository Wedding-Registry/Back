package com.wedding.serviceapi.boards.dto.weddinghall;

import com.wedding.serviceapi.boards.domain.Boards;
import com.wedding.serviceapi.boards.domain.HusbandAndWifeEachInfo;
import org.assertj.core.api.Assertions;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.groups.Tuple.*;
import static org.junit.jupiter.api.Assertions.*;

class WeddingHallInfoDtoTest {

    @Test
    @DisplayName("웨딩 게시판 정보 생성 시 남편 이름만 없는 경우")
    void WeddingHallInfoDto() {
        // given
        Boards boards = Boards.builder()
                .address("address")
                .date("date")
                .time("time")
                .husband(new HusbandAndWifeEachInfo("husband", null, "account1"))
                .wife(new HusbandAndWifeEachInfo("wife", null, "account2"))
                .build();
        // when
        WeddingHallInfoDto weddingHallInfoDto = new WeddingHallInfoDto(boards);
        // then
        Assertions.assertThat(weddingHallInfoDto.getAccount()).hasSize(2)
                .extracting("gender", "name", "bank", "account")
                .containsExactly(
                        tuple("husband", "husband", null, "account1"),
                        tuple("wife", "wife", null, "account2")
                );
    }
}