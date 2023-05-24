package com.wedding.serviceapi.admin.dto.summary;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class AttendanceSummaryDtoTest {

    @Test
    @DisplayName("emptyGuests 정적 메서드로 생성 시 각 값이 모두 0인지 확인")
    void emptyGuestsMakeZeroValue() {
        // given
        AttendanceSummaryDto test = AttendanceSummaryDto.emptyGuests();
        // then
        assertThat(test.getTotal()).isZero();
        assertThat(test.getYesRate()).isZero();
    }
}