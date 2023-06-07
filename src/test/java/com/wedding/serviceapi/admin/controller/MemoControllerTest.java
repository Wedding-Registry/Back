package com.wedding.serviceapi.admin.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wedding.serviceapi.WithCustomMockUser;
import com.wedding.serviceapi.admin.dto.memo.PadContentsDto;
import com.wedding.serviceapi.admin.service.MemoService;
import com.wedding.serviceapi.boards.domain.Boards;
import com.wedding.serviceapi.goods.dto.UsersGoodsPostResponseDto;
import com.wedding.serviceapi.goods.service.UsersGoodsService;
import com.wedding.serviceapi.goods.vo.PostUsersGoodsRequestVo;
import com.wedding.serviceapi.users.domain.LoginType;
import com.wedding.serviceapi.users.domain.Users;
import lombok.val;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
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

    @Test
    @DisplayName("메모를 정상적으로 반환 성공")
    @WithCustomMockUser
    void getMemoPad() throws Exception {
        // given
        String url = "/admin/memo/pad";
        Users user = Users.builder().name("testUser").loginType(LoginType.SERVICE).build();
        Boards board = Boards.builder().uuidFirst("uuidFirst").uuidSecond("uuidSecond").users(user).boardsMemo("test memo").build();
        PadContentsDto result = PadContentsDto.from(board);
        doReturn(result).when(memoService).getMemoContents(1L, 1L);
        // when
        ResultActions resultActions = mockMvc.perform(get(url)
                .contentType(MediaType.APPLICATION_JSON));

        // then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("success").value(true))
                .andExpect(jsonPath("status").value(200))
                .andExpect(jsonPath("data.contents").value("test memo"))
                .andDo(print());
    }

    @ParameterizedTest
    @ValueSource(strings = {" ", ""})
    @DisplayName("메모 패드 입력 시 빈 값으로 입력하면 실패")
    @WithCustomMockUser
    void postMemoPadFail(String memo) throws Exception {
        // given
        String url = "/admin/memo/pad";
        PadContentsDto content = new PadContentsDto(memo);
        // when
        ResultActions resultActions = mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(content)));
        // then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("success").value(false))
                .andExpect(jsonPath("status").value(400))
                .andExpect(jsonPath("message").value("메모를 입력해주세요"))
                .andDo(print());
    }

    @ParameterizedTest
    @ValueSource(strings = {"memo 1", "-1"})
    @DisplayName("메모 패드 입력 시 성공")
    @WithCustomMockUser
    void postMemoPadSuccess(String memo) throws Exception {
        // given
        String url = "/admin/memo/pad";
        PadContentsDto content = new PadContentsDto(memo);
        doReturn(content).when(memoService).postMemoContents(1L, 1L, memo);
        // when
        ResultActions resultActions = mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(content)));
        // then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("success").value(true))
                .andExpect(jsonPath("status").value(201))
                .andExpect(jsonPath("data.contents").value(memo))
                .andDo(print());
    }

    @Test
    @DisplayName("메모 패드 삭제 성공")
    @WithCustomMockUser
    void deleteMemoPadSuccess() throws Exception {
        // given
        String url = "/admin/memo/pad";
        doNothing().when(memoService).deleteMemoContents(1L, 1L);
        // when
        ResultActions resultActions = mockMvc.perform(delete(url)
                .contentType(MediaType.APPLICATION_JSON));
        // then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("success").value(true))
                .andExpect(jsonPath("status").value(202))
                .andExpect(jsonPath("data").isEmpty())
                .andDo(print());
    }

}