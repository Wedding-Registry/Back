package com.wedding.serviceapi.boards.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


import java.time.DateTimeException;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class BoardsTest {

    @Test
    @DisplayName("날짜 시간 설정 메서드 테스트")
    void setDateAndTime() {
        // given
        Boards boards = new Boards();
        String date = "20231112";
        String time = "1500";

        // when
        boards.updateDateAndTime(date, time);

        // then
        assertThat(boards.getDate()).isEqualTo("2023-11-12");
        assertThat(boards.getTime()).isEqualTo("15:00");
    }

    @Test
    @DisplayName("잘못된 날짜 정보가 들어온 경우")
    void illegalDateTimeInfo() {
        // given
        Boards boards = new Boards();
        String date = "00231112";
        String time = "1500";

        // when
        assertThrows(IllegalArgumentException.class, () -> boards.updateDateAndTime(date, time));
    }


    @Test
    @DisplayName("날짜 형식이 아닌 경우")
    void notValidDateTime() {
        // given
        Boards boards = new Boards();
        String date = "12345678";
        String time = "5432";

        // when
        assertThrows(DateTimeException.class, () -> boards.updateDateAndTime(date, time));
    }
}


















