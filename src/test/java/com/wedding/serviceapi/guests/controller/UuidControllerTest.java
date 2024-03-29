package com.wedding.serviceapi.guests.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wedding.serviceapi.WithCustomMockUser;
import com.wedding.serviceapi.guests.dto.GuestInfoJwtDto;
import com.wedding.serviceapi.guests.service.UuidService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.cookie;

@WebMvcTest(UuidController.class)
@MockBean(JpaMetamodelMappingContext.class)
@AutoConfigureMockMvc(addFilters = false)
class UuidControllerTest {

    ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    MockMvc mockMvc;
    @MockBean
    UuidService uuidService;

    @Test
    @DisplayName("uuidFirst만 보내게 되면 에러를 발생시킨다.")
    void makeJwtWithoutUuidSecond() throws Exception {
        // given
        String url = "/invitation/uuids/info";
        // when
        ResultActions resultActions = mockMvc.perform(get(url)
                .param("uuidFirst", "testUuidFirst"));
        // then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("success").value(false))
                .andExpect(jsonPath("status").value(400))
                .andExpect(jsonPath("message").value("uuidSecond 값이 없습니다."))
                .andDo(print());
    }

    @Test
    @DisplayName("uuidSecond만 보내게 되면 에러를 발생시킨다.")
    void makeJwtWithoutUuidFirst() throws Exception {
        // given
        String url = "/invitation/uuids/info";
        // when
        ResultActions resultActions = mockMvc.perform(get(url)
                .param("uuidSecond", "testUuidSecond"));
        // then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("success").value(false))
                .andExpect(jsonPath("status").value(400))
                .andExpect(jsonPath("message").value("uuidFirst 값이 없습니다."))
                .andDo(print());
    }

    @Test
    @DisplayName("uuidFirst, uuidSecond 를 보내면 jwt 토큰을 만들어서 응답한다.")
    @WithCustomMockUser
    void makeJwt() throws Exception {
        // given
        String url = "/invitation/uuids/info";
        GuestInfoJwtDto data = GuestInfoJwtDto.of("test");
        doReturn(data).when(uuidService).makeGuestInfoJwt("uuidFirst", "uuidSecond", 1L);
        // when
        ResultActions resultActions = mockMvc.perform(get(url)
                .param("uuidFirst", "uuidFirst")
                .param("uuidSecond", "uuidSecond"));
        // then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("success").value(true))
                .andExpect(jsonPath("status").value(200))
                .andExpect(jsonPath("data.guestInfoJwt").value("test"))
                .andDo(print());
    }
}