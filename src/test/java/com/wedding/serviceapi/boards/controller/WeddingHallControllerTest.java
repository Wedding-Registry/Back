package com.wedding.serviceapi.boards.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wedding.serviceapi.boards.domain.Boards;
import com.wedding.serviceapi.boards.domain.HusbandAndWifeEachInfo;
import com.wedding.serviceapi.boards.dto.weddinghall.*;
import com.wedding.serviceapi.boards.service.WeddingHallService;
import com.wedding.serviceapi.boards.vo.RequestPostWeddingHallTimeVo;
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

    @Test
    @DisplayName("예식장 정보 가져오기")
    void getWeddingHallInfo() throws Exception {
        // given
        WeddingHallInfoDto weddingHallInfoDto = makeResultData();
        doReturn(weddingHallInfoDto).when(weddingHallService).getWeddingHallInfo(anyLong());

        // when
        ResultActions resultActions = mockMvc.perform(get("/weddingHall/all/{boardsId}", anyLong()));

        // then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("status").value(200))
                .andDo(print());
    }

    private WeddingHallInfoDto makeResultData() {
        WeddingHallInfoDto testResult = new WeddingHallInfoDto();
        testResult.getUsers().add(new HusbandWifeNameDto("husband", "husband name"));
        testResult.getUsers().add(new HusbandWifeNameDto("wife", "wife name"));
        testResult.getAccount().add(new HusbandWifeBankAccountDto("husband", "husband bank", "husband account"));
        testResult.getAccount().add(new HusbandWifeBankAccountDto("wife", "wife bank", "wife account"));
        testResult.setLocation("test address");
        testResult.setWeddingDate("2023-02-16");
        testResult.setWeddingTime("15:50");
        return testResult;
    }

    @Test
    @DisplayName("예식장 주소 변경")
    void postWeddingHallAddressSuccess() throws Exception {
        // given
        String newAddress = "testAddress";
        WeddingHallAddressDto data = new WeddingHallAddressDto(newAddress);

        doReturn(data).when(weddingHallService).postWeddingHallAddress(1L, newAddress);

        // when
        ResultActions resultActions = mockMvc.perform(post("/weddingHall/location/{boardsId}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(newAddress)
        );

        // then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("data.address").value("testAddress"))
                .andDo(print());
    }

    @Test
    @DisplayName("예식장 날짜 시간 변경")
    void postWeddingHallDateTimeSuccess() throws Exception {
        // given
        String date = "20231115";
        String time = "1550";
        RequestPostWeddingHallTimeVo requestVo = new RequestPostWeddingHallTimeVo(date, time);
        WeddingHallDateTimeDto data = new WeddingHallDateTimeDto("2023-11-15", "15:50");

        doReturn(data).when(weddingHallService).postWeddingHallDateTime(1L, date, time);

        // when
        ResultActions resultActions = mockMvc.perform(post("/weddingHall/time/{boardsId}", 1L)
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
}
























