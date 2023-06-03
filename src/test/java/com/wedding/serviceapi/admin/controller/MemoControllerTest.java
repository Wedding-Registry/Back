package com.wedding.serviceapi.admin.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wedding.serviceapi.WithCustomMockUser;
import com.wedding.serviceapi.admin.service.MemoService;
import com.wedding.serviceapi.goods.dto.UsersGoodsPostResponseDto;
import com.wedding.serviceapi.goods.service.UsersGoodsService;
import com.wedding.serviceapi.goods.vo.PostUsersGoodsRequestVo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.stubbing.Stubber;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MemoController.class)
@MockBean(JpaMetamodelMappingContext.class)
@AutoConfigureMockMvc(addFilters = false)
class MemoControllerTest {

    ObjectMapper objectMapper = new ObjectMapper();

    @MockBean
    MemoService memoService;
    @MockBean
    UsersGoodsService usersGoodsService;

    @Autowired
    MockMvc mockMvc;

    @Test
    @DisplayName("메모 아이템 등록 성공")
    @WithCustomMockUser
    void postMemoItem() throws Exception {
        // given
        String url = "/admin/memo/item/wish";
        UsersGoodsPostResponseDto responseDto = new UsersGoodsPostResponseDto(1L, "testUrl", "testName", 10000);
        PostUsersGoodsRequestVo requestVo = new PostUsersGoodsRequestVo("testUrl");
        doReturn(responseDto).when(usersGoodsService).postUsersGoods(1L, requestVo.getUrl(), 1L, true);
        // when
        ResultActions resultActions = mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestVo)));
        // then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("success").value(true))
                .andExpect(jsonPath("status").value(201))
                .andExpect(jsonPath("data.usersGoodsImgUrl").value("testUrl"))
                .andDo(print());
    }

}