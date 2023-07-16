package com.wedding.serviceapi.admin.controller;

import com.wedding.serviceapi.WithCustomMockUser;
import com.wedding.serviceapi.admin.dto.summary.AttendanceSummaryDto;
import com.wedding.serviceapi.admin.dto.summary.DonationSummaryDto;
import com.wedding.serviceapi.admin.service.SummaryService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@WebMvcTest(SummaryController.class)
@MockBean(JpaMetamodelMappingContext.class)
@AutoConfigureMockMvc(addFilters = false)
class SummaryControllerTest {

    @MockBean
    SummaryService summaryService;

    @Autowired
    MockMvc mockMvc;

    @DisplayName("참석 여부 요약 api 테스트")
    @WithCustomMockUser
    @ParameterizedTest
    @ValueSource(ints = {0, 1})
    void getAttendanceSummary(int total) throws Exception {
        // given
        String url = "/admin/summary/attendance";

        AttendanceSummaryDto result;
        if (total == 0) {
            result = AttendanceSummaryDto.emptyGuests();
        } else {
            result = AttendanceSummaryDto.of(total, 1, 1, 1, 1, 1, 1);
        }
        Mockito.doReturn(result).when(summaryService).getAttendanceSummary(1L);
        // when
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get(url)
                .contentType(MediaType.APPLICATION_JSON));
        // then
        resultActions.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("success").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("status").value(200))
                .andExpect(MockMvcResultMatchers.jsonPath("data.total").value(total))
                .andDo(MockMvcResultHandlers.print());
    }
}