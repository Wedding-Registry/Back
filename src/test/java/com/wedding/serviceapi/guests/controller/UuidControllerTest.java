package com.wedding.serviceapi.guests.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wedding.serviceapi.WithCustomMockUser;
import com.wedding.serviceapi.guests.dto.UuidRequestDto;
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
    @DisplayName("uuidFirst, uuidSecond를 body에 담아서 요청하면 쿠키에 boardsId를 세팅해줍니다.")
    @WithCustomMockUser
    void setBoardsIdCookie() throws Exception {
        // given
        String url = "/invitation/uuids";
        UuidRequestDto requestBody = UuidRequestDto.builder()
                .uuidFirst("first")
                .uuidSecond("second")
                .build();

        doAnswer(invocation -> {
            HttpServletResponse res = invocation.getArgument(2);
            res.addCookie(new Cookie("boardsId", "1"));
            return null;
        }).when(uuidService).setBoardsIdCookie(anyString(), anyString(), any(MockHttpServletResponse.class));
        // when
        ResultActions resultActions = mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestBody)));
        // then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("success").value(true))
                .andExpect(jsonPath("status").value(200))
                .andExpect(jsonPath("data").isEmpty())
                .andExpect(cookie().exists("boardsId"))
                .andExpect(cookie().value("boardsId", "1"))
                .andDo(print());
    }

}