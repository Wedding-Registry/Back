package com.wedding.serviceapi.goods.controller;

import com.wedding.serviceapi.goods.dto.UsersGoodsPostResponseDto;
import com.wedding.serviceapi.goods.service.UsersGoodsService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
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

@WebMvcTest(UsersGoodsController.class)
@MockBean(JpaMetamodelMappingContext.class)
@AutoConfigureMockMvc(addFilters = false)
class UsersGoodsControllerTest {

    @MockBean
    UsersGoodsService usersGoodsService;

    @Autowired
    MockMvc mockMvc;

    @Test
    @DisplayName("상품 URL 등록 성공")
    void postUsersGoods() throws Exception {
        // given
        Long userId = 1L;
        String url = "testUrl";
        UsersGoodsPostResponseDto usersGoodsPostResponseDto = new UsersGoodsPostResponseDto();

        BDDMockito.given(usersGoodsService.postUsersGoods(userId, url)).willReturn(usersGoodsPostResponseDto);

        // when
        ResultActions resultActions = mockMvc.perform(post("/usersgoods/add/{userId}", 1)
                .content(url)
                .contentType(MediaType.APPLICATION_JSON)
        );

        // given
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("status").value(201))
                .andDo(print());
    }

    @Test
    @DisplayName("상품 이름 변경 성공")
    void updateUsersGoods() throws Exception {
        // given
        Long userId = 1L;
        Long usersGoodsId = 1L;
        String usersGoodsName = "testGoodsName";

        doNothing().when(usersGoodsService).updateUsersGoodsName(usersGoodsId, usersGoodsName);

        // when
        ResultActions resultActions = mockMvc.perform(post("/usersgoods/name/update/{userId}", userId)
                .param("usersGoodsId", "1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(usersGoodsName)
        );

        // then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("success").value(true))
                .andExpect(jsonPath("status").value(201))
                .andExpect(jsonPath("data").doesNotExist())
                .andDo(print());
    }
}


























