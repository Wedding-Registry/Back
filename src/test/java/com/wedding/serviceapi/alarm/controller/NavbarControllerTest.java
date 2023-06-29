package com.wedding.serviceapi.alarm.controller;

import com.wedding.serviceapi.WithCustomMockUser;
import com.wedding.serviceapi.alarm.service.NavbarService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(NavbarController.class)
@MockBean(JpaMetamodelMappingContext.class)
@AutoConfigureMockMvc(addFilters = false)
class NavbarControllerTest {

    @MockBean
    NavbarService navbarService;

    @Autowired
    MockMvc mockMvc;

    @Test
    @DisplayName("네비게이션 바에 필요한 모든 알람 리스트를 가져옵니다.")
    @WithCustomMockUser
    void getNavbarAlarm() throws Exception {
        // given
        String url = "/navbar/alarm/all";
        Long boardsId = 1L;
        // when
        ResultActions resultActions = mockMvc.perform(get(url));
        // then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("success").value(true))
                .andExpect(jsonPath("status").value(200))
                .andDo(MockMvcResultHandlers.print());
    }

}