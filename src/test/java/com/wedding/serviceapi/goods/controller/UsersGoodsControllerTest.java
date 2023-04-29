package com.wedding.serviceapi.goods.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wedding.serviceapi.auth.securitycustom.AuthUser;
import com.wedding.serviceapi.auth.vo.LoginUserVo;
import com.wedding.serviceapi.common.exceptionhandler.GlobalExceptionHandler;
import com.wedding.serviceapi.common.resolver.AuthenticationArgumentResolver;
import com.wedding.serviceapi.goods.dto.UsersGoodsNameDto;
import com.wedding.serviceapi.goods.dto.UsersGoodsPostResponseDto;
import com.wedding.serviceapi.goods.dto.UsersGoodsPriceDto;
import com.wedding.serviceapi.goods.service.UsersGoodsService;
import com.wedding.serviceapi.goods.vo.PostUsersGoodsRequestVo;
import com.wedding.serviceapi.goods.vo.UpdateUsersGoodsNameRequestVo;
import com.wedding.serviceapi.goods.vo.UpdateUsersGoodsPriceRequestVo;
import com.wedding.serviceapi.users.domain.Role;
import com.wedding.serviceapi.users.domain.Users;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithSecurityContext;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
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

    @Test
    @DisplayName("상품 URL 등록 성공")
    @WithCustomMockUser(username = "test")
    void postUsersGoods() throws Exception {
        // given
        Long userId = 1L;
        String url = "testUrl";
        PostUsersGoodsRequestVo requestVo = new PostUsersGoodsRequestVo(url);
        UsersGoodsPostResponseDto usersGoodsPostResponseDto = new UsersGoodsPostResponseDto();

        BDDMockito.given(usersGoodsService.postUsersGoods(userId, url)).willReturn(usersGoodsPostResponseDto);

        // when
        ResultActions resultActions = mockMvc.perform(post("/usersgoods/add")
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
    @WithCustomMockUser(username = "test")
    void postUsersGoodsFail() throws Exception {
        // given
        Long userId = 1L;
        String url = "testUrl";
        PostUsersGoodsRequestVo requestVo = new PostUsersGoodsRequestVo(url);

        when(usersGoodsService.postUsersGoods(userId, url)).thenThrow(new IllegalArgumentException("잘못된 url 정보입니다."));
        // when
        ResultActions resultActions = mockMvc.perform(post("/usersgoods/add")
//                .with(authentication(authentication))
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
    @WithCustomMockUser(username = "test")
    void updateUsersGoods() throws Exception {
        // given
        Long userId = 1L;
        Long usersGoodsId = 1L;
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
    @WithCustomMockUser(username = "test")
    void updateUsersGoodsPrice() throws Exception {
        // given
        Long userId = 1L;
        Long usersGoodsId = 1L;
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
        Long usersGoodsId = 1L;

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


























