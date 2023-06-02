package com.wedding.serviceapi.admin.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wedding.serviceapi.WithCustomMockUser;
import com.wedding.serviceapi.admin.dto.alarm.AlarmListResponseDto;
import com.wedding.serviceapi.admin.dto.alarm.DonationUserInfoDto;
import com.wedding.serviceapi.admin.dto.attendance.AttendanceUserInfoDto;
import com.wedding.serviceapi.admin.service.AlarmService;
import com.wedding.serviceapi.boards.domain.Boards;
import com.wedding.serviceapi.guests.domain.AttendanceType;
import com.wedding.serviceapi.guests.domain.GoodsDonation;
import com.wedding.serviceapi.guests.domain.Guests;
import com.wedding.serviceapi.users.domain.Users;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
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
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AlarmController.class)
@MockBean(JpaMetamodelMappingContext.class)
@AutoConfigureMockMvc(addFilters = false)
class AlarmControllerTest {

    @MockBean
    AlarmService alarmService;

    @Autowired
    MockMvc mockMvc;

    @Test
    @DisplayName("모든 알람을 가져오는지 확인하는 테스트")
    @WithCustomMockUser
    void getAllAlarmList() throws Exception {
        // given
        String url = "/admin/alarm";
        Long boardsId = 1L;

        AlarmListResponseDto result = AlarmListResponseDto.from(List.of(), List.of());
        doReturn(result).when(alarmService).getAllAlarmList(boardsId);
        // when
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get(url)
                .contentType(MediaType.APPLICATION_JSON));
        // then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("success").value(true))
                .andExpect(jsonPath("status").value(200))
                .andExpect(jsonPath("data.size()").value(2))
                .andDo(MockMvcResultHandlers.print());
    }
}