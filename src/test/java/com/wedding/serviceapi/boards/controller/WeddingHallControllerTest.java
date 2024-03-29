package com.wedding.serviceapi.boards.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wedding.serviceapi.WithCustomMockUser;
import com.wedding.serviceapi.boards.dto.weddinghall.*;
import com.wedding.serviceapi.boards.service.WeddingHallService;
import com.wedding.serviceapi.boards.vo.weddinghall.PostWeddingHallAddressRequestVo;
import com.wedding.serviceapi.boards.vo.weddinghall.RequestPostWeddingHallTimeVo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;


import java.time.DateTimeException;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(WeddingHallController.class)
@MockBean(JpaMetamodelMappingContext.class)
@AutoConfigureMockMvc(addFilters = false)
class WeddingHallControllerTest {

    @MockBean
    WeddingHallService weddingHallService;

    @Autowired
    MockMvc mockMvc;

    ObjectMapper objectMapper = new ObjectMapper();

    private Long boardsId;
    private Long usersId;

    @BeforeEach
    void init() {
        boardsId = 1L;
        usersId = 1L;
    }

    @Test
    @DisplayName("예식장 정보 가져오기")
    @WithCustomMockUser
    void getWeddingHallInfo() throws Exception {
        // given
        WeddingHallInfoDto weddingHallInfoDto = makeResultData();
        doReturn(weddingHallInfoDto).when(weddingHallService).getWeddingHallInfo(boardsId, usersId);

        // when
        ResultActions resultActions = mockMvc.perform(get("/weddingHall/all"));

        // then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("status").value(200))
                .andDo(print());
    }

    private WeddingHallInfoDto makeResultData() {
        WeddingHallInfoDto testResult = new WeddingHallInfoDto();
        testResult.getAccount().add(new HusbandWifeBankAccountDto("husband", "husband name", "husband bank", "husband account"));
        testResult.getAccount().add(new HusbandWifeBankAccountDto("wife", "wife name", "wife bank", "wife account"));
        testResult.setLocation("test address");
        testResult.setWeddingDate("2023-02-16");
        testResult.setWeddingTime("15:50");
        return testResult;
    }

    @Test
    @DisplayName("예식장 주소 변경")
    @WithCustomMockUser
    void postWeddingHallAddressSuccess() throws Exception {
        // given
        String newAddress = "testAddress";
        WeddingHallAddressDto data = new WeddingHallAddressDto(newAddress);

        PostWeddingHallAddressRequestVo requestVo = new PostWeddingHallAddressRequestVo(newAddress);

        doReturn(data).when(weddingHallService).postWeddingHallAddress(boardsId, newAddress, usersId);

        // when
        ResultActions resultActions = mockMvc.perform(post("/weddingHall/location")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestVo))
        );

        // then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("data.address").value("testAddress"))
                .andDo(print());
    }

    @Test
    @DisplayName("예식장 날짜 시간 변경")
    @WithCustomMockUser
    void postWeddingHallDateTimeSuccess() throws Exception {
        // given
        String date = "20231115";
        String time = "1550";
        RequestPostWeddingHallTimeVo requestVo = new RequestPostWeddingHallTimeVo(date, time);
        WeddingHallDateTimeDto data = new WeddingHallDateTimeDto("2023-11-15", "15:50");

        doReturn(data).when(weddingHallService).postWeddingHallDateTime(boardsId, date, time, usersId);

        // when
        ResultActions resultActions = mockMvc.perform(post("/weddingHall/time")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestVo))
        );

        // then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("status").value(201))
                .andExpect(jsonPath("data.weddingDate").value("2023-11-15"))
                .andExpect(jsonPath("data.weddingTime").value("15:50"))
                .andDo(print());
    }

    @Test
    @DisplayName("예식장 날짜 시간 변경 실패")
    @WithCustomMockUser
    void postWeddingHallDateTimeFail() throws Exception {
        // given
        RequestPostWeddingHallTimeVo requestVo = new RequestPostWeddingHallTimeVo("date", "time");
        when(weddingHallService.postWeddingHallDateTime(boardsId, "date", "time", usersId)).thenThrow(DateTimeException.class);

        // when
        ResultActions resultActions = mockMvc.perform(post("/weddingHall/time")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestVo))
        );

        // then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("success").value(false))
                .andExpect(jsonPath("status").value(400))
                .andExpect(jsonPath("message").value("날짜, 시간 정보가 정확하지 않습니다."))
                .andDo(print());
    }
}
























