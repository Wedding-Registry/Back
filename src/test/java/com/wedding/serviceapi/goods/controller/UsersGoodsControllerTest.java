package com.wedding.serviceapi.goods.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wedding.serviceapi.WithCustomMockUser;
import com.wedding.serviceapi.boards.domain.Boards;
import com.wedding.serviceapi.goods.dto.MakeBoardResponseDto;
import com.wedding.serviceapi.goods.dto.UsersGoodsNameDto;
import com.wedding.serviceapi.goods.dto.UsersGoodsPostResponseDto;
import com.wedding.serviceapi.goods.dto.UsersGoodsPriceDto;
import com.wedding.serviceapi.goods.service.UsersGoodsService;
import com.wedding.serviceapi.goods.vo.PostUsersGoodsRequestVo;
import com.wedding.serviceapi.goods.vo.UpdateUsersGoodsNameRequestVo;
import com.wedding.serviceapi.goods.vo.UpdateUsersGoodsPriceRequestVo;
import org.junit.jupiter.api.BeforeEach;
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

    ObjectMapper objectMapper = new ObjectMapper();

    @MockBean
    UsersGoodsService usersGoodsService;

    @Autowired
    MockMvc mockMvc;

    Long userId;
    Long boardsId;
    Long usersGoodsId;
    String userName;

    @BeforeEach
    void init() {
        userId = 1L;
        userName = "test";
        boardsId = 1L;
        usersGoodsId = 1L;
    }

    @Test
    @DisplayName("웨딩 게시판 만들기 실패")
    @WithCustomMockUser
    void failMakeWeddingBoard() throws Exception {
        // given
        when(usersGoodsService.makeWeddingBoard(userId, userName)).thenThrow(new IllegalArgumentException("이미 존재하는 게시판입니다."));
        // when
        ResultActions resultActions = mockMvc.perform(get("/usersgoods/add/board")
                .contentType(MediaType.APPLICATION_JSON));
        // then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("status").value(400))
                .andExpect(jsonPath("success").value(false))
                .andExpect(jsonPath("message").value("이미 존재하는 게시판입니다."))
                .andDo(print());
    }

    @Test
    @DisplayName("웨딩 게시판 만들기 성공")
    @WithCustomMockUser
    void successMakeWeddingBoard() throws Exception {
        // given
        Boards boards = Boards.builder().id(boardsId).uuidFirst("first").uuidSecond("second").build();
        MakeBoardResponseDto data = new MakeBoardResponseDto(boards, "accessToken", "refreshToken");
        doReturn(data).when(usersGoodsService).makeWeddingBoard(userId, userName);
        // when
        ResultActions resultActions = mockMvc.perform(get("/usersgoods/add/board")
                .contentType(MediaType.APPLICATION_JSON));

        // then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("success").value(true))
                .andExpect(jsonPath("status").value(201))
                .andExpect(jsonPath("data.uuidFirst").value("first"))
                .andExpect(jsonPath("data.uuidSecond").value("second"));
    }

    @Test
    @DisplayName("상품 URL 등록 성공")
    @WithCustomMockUser
    void postUsersGoods() throws Exception {
        // given
        String url = "testUrl";
        PostUsersGoodsRequestVo requestVo = new PostUsersGoodsRequestVo(url);
        UsersGoodsPostResponseDto usersGoodsPostResponseDto = new UsersGoodsPostResponseDto();

        BDDMockito.given(usersGoodsService.postUsersGoods(userId, url, boardsId, false)).willReturn(usersGoodsPostResponseDto);

        // when
        ResultActions resultActions = mockMvc.perform(post("/usersgoods/add/product")
                .content(objectMapper.writeValueAsString(requestVo))
                .contentType(MediaType.APPLICATION_JSON)
        );

        // given
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("status").value(201))
                .andDo(print());
    }

    @Test
    @DisplayName("상품 URL 등록 실패")
    @WithCustomMockUser
    void postUsersGoodsFail() throws Exception {
        // given
        String url = "testUrl";
        PostUsersGoodsRequestVo requestVo = new PostUsersGoodsRequestVo(url);

        when(usersGoodsService.postUsersGoods(userId, url, boardsId, false)).thenThrow(new IllegalArgumentException("잘못된 url 정보입니다."));
        // when
        ResultActions resultActions = mockMvc.perform(post("/usersgoods/add/product")
                .content(objectMapper.writeValueAsString(requestVo))
                .contentType(MediaType.APPLICATION_JSON)
        );

        // given
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("success").value(false))
                .andExpect(jsonPath("status").value(400))
                .andExpect(jsonPath("message").value("잘못된 url 정보입니다."))
                .andDo(print());
    }

    @Test
    @DisplayName("상품 이름 변경 성공")
    @WithCustomMockUser
    void updateUsersGoods() throws Exception {
        // given

        String usersGoodsName = "testGoodsName";
        UpdateUsersGoodsNameRequestVo requestVo = new UpdateUsersGoodsNameRequestVo(usersGoodsName);
        UsersGoodsNameDto data = new UsersGoodsNameDto(usersGoodsName);

        doReturn(data).when(usersGoodsService).updateUsersGoodsName(userId, usersGoodsId, requestVo.getUsersGoodsName());

        // when
        ResultActions resultActions = mockMvc.perform(post("/usersgoods/name/update")
                .param("usersGoodsId", "1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestVo))
        );

        // then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("success").value(true))
                .andExpect(jsonPath("status").value(201))
                .andExpect(jsonPath("data.usersGoodsName").value("testGoodsName"))
                .andDo(print());
    }

    @Test
    @DisplayName("상품 후원가 변경 성공")
    @WithCustomMockUser
    void updateUsersGoodsPrice() throws Exception {
        // given
        Integer newPrice = 1000;

        UpdateUsersGoodsPriceRequestVo requestVo = new UpdateUsersGoodsPriceRequestVo(newPrice);
        UsersGoodsPriceDto data = new UsersGoodsPriceDto(newPrice);
        doReturn(data).when(usersGoodsService).updateUsersGoodsPrice(userId, usersGoodsId, newPrice);

        // when
        ResultActions resultActions = mockMvc.perform(post("/usersgoods/cost/update")
                .param("usersGoodsId", "1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestVo))
        );

        // then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("success").value(true))
                .andExpect(jsonPath("status").value(201))
                .andExpect(jsonPath("data.usersGoodsPrice").value(1000))
                .andDo(print());
    }

    @Test
    @DisplayName("등록된 상품 삭제 성공")
    void deleteUsersGoods() throws Exception {
        // given
        doNothing().when(usersGoodsService).deleteUsersGoods(usersGoodsId);

        // when
        ResultActions resultActions = mockMvc.perform(delete("/usersgoods")
                .param("usersGoodsId", "1")
                .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("success").value(true))
                .andExpect(jsonPath("status").value(202))
                .andExpect(jsonPath("data").doesNotExist())
                .andDo(print());
    }
}


























