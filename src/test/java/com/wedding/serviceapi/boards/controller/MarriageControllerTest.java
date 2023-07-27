package com.wedding.serviceapi.boards.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wedding.serviceapi.WithCustomMockUser;
import com.wedding.serviceapi.boards.dto.marriage.MarriageBankAccountDto;
import com.wedding.serviceapi.boards.dto.marriage.MarriageNameDto;
import com.wedding.serviceapi.boards.service.MarriageService;
import com.wedding.serviceapi.boards.vo.marriage.PostHusbandOrWifeNameRequestVo;
import com.wedding.serviceapi.boards.vo.marriage.RequestPostMarriageBankAccountVo;
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

@WebMvcTest(MarriageController.class)
@MockBean(JpaMetamodelMappingContext.class)
@AutoConfigureMockMvc(addFilters = false)
class MarriageControllerTest {

    ObjectMapper objectMapper = new ObjectMapper();

    @MockBean
    MarriageService marriageService;

    @Autowired
    MockMvc mockMvc;

    @Test
    @DisplayName("신랑 혹인 신부 이름 수정")
    @WithCustomMockUser
    void changeHusbandOrWifeName() throws Exception {
        // given
        String type = "husband";
        Long boardsId = 1L;
        String name = "husband name";
        MarriageNameDto data = new MarriageNameDto(name);
        PostHusbandOrWifeNameRequestVo requestVo = new PostHusbandOrWifeNameRequestVo(name);
        Long userId = 1L;

        doReturn(data).when(marriageService).postHusbandOrWifeName(type, boardsId, name, userId);
        // when
        ResultActions resultActions = mockMvc.perform(post("/marriage/{type}/name/", type)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestVo))
        );

        // then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("data.name").value("husband name"))
                .andExpect(jsonPath("status").value(201))
                .andDo(print());
    }

    @Test
    @DisplayName("신랑 혹은 신부 은행 정보 수정")
    @WithCustomMockUser
    void changeHusbandOrWifeBandAndAccount() throws Exception {
        // given
        String type = "husband";
        Long boardsId = 1L;
        RequestPostMarriageBankAccountVo body = new RequestPostMarriageBankAccountVo("bank test", "account test");
        MarriageBankAccountDto data = new MarriageBankAccountDto(body.getBank(), body.getAccount());
        Long userId = 1L;

        doReturn(data).when(marriageService).postMarriageBankAndAccount(type, boardsId, data.getBank(), data.getAccount(), userId);
        // when
        ResultActions resultActions = mockMvc.perform(post("/marriage/{type}/account/", type)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(body))
        );
        // then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("data.bank").value("bank test"))
                .andExpect(jsonPath("data.account").value("account test"))
                .andExpect(jsonPath("status").value(201))
                .andDo(print());
    }
}























