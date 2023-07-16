package com.wedding.serviceapi.admin.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wedding.serviceapi.WithCustomMockUser;
import com.wedding.serviceapi.admin.dto.attendance.AttendanceInfoDto;
import com.wedding.serviceapi.admin.dto.attendance.AttendanceResponseDto;
import com.wedding.serviceapi.admin.dto.attendance.ChangeAttendanceDto;
import com.wedding.serviceapi.admin.service.AttendanceService;
import com.wedding.serviceapi.admin.vo.ChangeAttendanceRequestVo;
import com.wedding.serviceapi.guests.domain.AttendanceType;
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
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AttendanceController.class)
@MockBean(JpaMetamodelMappingContext.class)
@AutoConfigureMockMvc(addFilters = false)
class AttendanceControllerTest {

    private ObjectMapper objectMapper = new ObjectMapper();

    @MockBean
    AttendanceService attendanceService;

    @Autowired
    MockMvc mockMvc;

    @ParameterizedTest
    @ValueSource(ints = {0, 1})
    @DisplayName("참석 여부 상세 테스트")
    @WithCustomMockUser
    void getAttendanceInfo(int total) throws Exception {
        // given
        String url = "/admin/attendance/detail";
        AttendanceResponseDto result;
        if (total == 0) {
            result = AttendanceResponseDto.emptyGuests();
        } else {
            AttendanceInfoDto yes = AttendanceInfoDto.of(1, 1, List.of());
            AttendanceInfoDto no = AttendanceInfoDto.of(1, 1, List.of());
            AttendanceInfoDto unknown = AttendanceInfoDto.of(1, 1, List.of());
            result = AttendanceResponseDto.of(yes, no, unknown);
        }
        doReturn(result).when(attendanceService).getAllAttendanceInfo(1L);
        // when
        ResultActions resultActions = mockMvc.perform(get(url)
                .contentType(MediaType.APPLICATION_JSON));
        // then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("success").value(true))
                .andExpect(jsonPath("status").value(200))
                .andExpect(jsonPath("data.size()").value(3));
    }

    @Test
    @DisplayName("참석 여부 수정 시 옳바르지 않은 type 으로 인한 실패")
    @WithCustomMockUser
    void noValidAttendanceType() throws Exception {
        // given
        String url = "/admin/attendance";
        List<ChangeAttendanceRequestVo> requestVo = List.of(new ChangeAttendanceRequestVo(1L, "yees"));
        // when
        ResultActions resultActions = mockMvc.perform(put(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestVo)));
        // then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("success").value(false))
                .andExpect(jsonPath("status").value(400))
                .andExpect(jsonPath("message").value("잘못된 참석 여부 입니다."))
                .andDo(print());
    }

    @ParameterizedTest
    @ValueSource(strings = {"yes", "Yes", "YES", "unKnown", "UNKNOWN", "No", "NO", "nO"})
    @DisplayName("참석 여부 수정 시 옳바른 타입으로 요청 성공")
    @WithCustomMockUser
    void validAttendanceType(String type) throws Exception {
        // given
        String url = "/admin/attendance";
        List<ChangeAttendanceRequestVo> requestVo = List.of(new ChangeAttendanceRequestVo(1L, type));

        AttendanceInfoDto yes = AttendanceInfoDto.of(1, 1, List.of());
        AttendanceInfoDto no = AttendanceInfoDto.of(1, 1, List.of());
        AttendanceInfoDto unknown = AttendanceInfoDto.of(1, 1, List.of());
        AttendanceResponseDto result = AttendanceResponseDto.of(yes, no, unknown);

        doReturn(result).when(attendanceService).changeAttendance(anyList(), anyLong());
        // when
        ResultActions resultActions = mockMvc.perform(put(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestVo)));
        // then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("success").value(true))
                .andExpect(jsonPath("status").value(202))
                .andExpect(jsonPath("data").isNotEmpty())
                .andDo(print());
    }
}