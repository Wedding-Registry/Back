package com.wedding.serviceapi.admin.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wedding.serviceapi.WithCustomMockUser;
import com.wedding.serviceapi.admin.dto.donation.AccountTransferInfoDto;
import com.wedding.serviceapi.admin.dto.donation.DonatedUsersGoodsInfoDto;
import com.wedding.serviceapi.admin.dto.donation.DonationGuestInfoDto;
import com.wedding.serviceapi.admin.service.DonationService;
import com.wedding.serviceapi.admin.vo.AccountTransferRequestVo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;
import java.util.stream.Stream;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(DonationController.class)
@MockBean(JpaMetamodelMappingContext.class)
@AutoConfigureMockMvc(addFilters = false)
class DonationControllerTest {

    private ObjectMapper objectMapper = new ObjectMapper();
    
    @MockBean
    DonationService donationService;

    @Autowired
    MockMvc mockMvc;


    @Test
    @DisplayName("상품후원 컨트롤러 정상 작동 테스트")
    @WithCustomMockUser
    void 상품_후원_조회_테스트() throws Exception {
        // given
        String url = "/admin/donation/product/detail";
        DonationGuestInfoDto donation = new DonationGuestInfoDto(1L, 2L, "guest1", 2000);
        DonatedUsersGoodsInfoDto donatedUsersGoodsInfoDto = new DonatedUsersGoodsInfoDto(1L, "test", 10000, "testImg", List.of(donation));
        doReturn(List.of(donatedUsersGoodsInfoDto)).when(donationService).findAllUsersGoodsInfo(1L, 1L);
        // when
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get(url)
                .contentType(MediaType.APPLICATION_JSON));

        // then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("success").value(true))
                .andExpect(jsonPath("status").value(200))
                .andExpect(jsonPath("data.length()").value(1))
                .andDo(print());
    }

    @Test
    @DisplayName("계좌이체 조회 컨트롤러 정상 작동 테스트")
    @WithCustomMockUser
    void 계좌_이체_조회_테스트() throws Exception {
        // given
        String url = "/admin/donation/transfer/detail";
        AccountTransferInfoDto testData = new AccountTransferInfoDto(1L, "test memo");
        doReturn(List.of(testData)).when(donationService).findAllAccountTransferInfo(1L);
        // when
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get(url)
                .contentType(MediaType.APPLICATION_JSON));
        // then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("success").value(true))
                .andExpect(jsonPath("status").value(200))
                .andExpect(jsonPath("data.size()").value(1))
                .andDo(print());
    }
    
    @Test
    @DisplayName("계좌이체메모 추가 시 메모 내용을 작성하지 않아서 실패하는 테스트")
    @WithCustomMockUser
    void 계좌이체메모추가실패_테스트() throws Exception {
        // given
        String url = "/admin/donation/transfer/detail";
        String transferMemo = "";
        AccountTransferRequestVo accountTransferRequestVo = new AccountTransferRequestVo(transferMemo);
        // when
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(accountTransferRequestVo)));
        // then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("success").value(false))
                .andExpect(jsonPath("status").value(400))
                .andExpect(jsonPath("message").value("메모를 추가해 주세요"));
    }

    @Test
    @DisplayName("계좌이체메모 추가 성공 테스트")
    @WithCustomMockUser
    void 계좌이체메모추가성공_테스트() throws Exception {
        // given
        String url = "/admin/donation/transfer/detail";
        String transferMemo = "test memo 입니다.";
        AccountTransferRequestVo accountTransferRequestVo = new AccountTransferRequestVo(transferMemo);
        AccountTransferInfoDto testData = new AccountTransferInfoDto(1L, transferMemo);
        doReturn(testData).when(donationService).postAccountTransferMemo(1L, transferMemo);
        // when
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(accountTransferRequestVo)));
        // then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("success").value(true))
                .andExpect(jsonPath("status").value(201))
                .andExpect(jsonPath("data.transferMemo").value("test memo 입니다."))
                .andDo(print());
    }

    @ParameterizedTest
    @WithCustomMockUser
    @MethodSource(value = "generate")
    @DisplayName("계좌이체메모 수정 필요한 데이터가 없어서 실패")
    void 계좌이체메모수정실패_테스트(AccountTransferInfoDto requestVo, String message) throws Exception {
        // given
        String url = "/admin/donation/transfer/detail";
        // when
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.put(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestVo))
        );
        // then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("success").value(false))
                .andExpect(jsonPath("status").value(400))
                .andExpect(jsonPath("message").value(message))
                .andDo(print());
    }

    private static Stream<Arguments> generate() {
        return Stream.of(
                Arguments.of(new AccountTransferInfoDto(null, "updated memo"), "메모 아이디가 필요합니다."),
                Arguments.of(new AccountTransferInfoDto(1L, ""), "메모를 작성해 주세요")
        );
    }

    @Test
    @DisplayName("계좌이체메모 수정 성공")
    @WithCustomMockUser
    void 계좌이체메모수정_성공() throws Exception {
        // given
        String url = "/admin/donation/transfer/detail";
        String transferMemo = "test memo 입니다.";
        AccountTransferInfoDto testData = new AccountTransferInfoDto(1L, transferMemo);
        doReturn(testData).when(donationService).putAccountTransferMemo(1L, 1L,  transferMemo);
        // when
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.put(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testData)));
        // then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("success").value(true))
                .andExpect(jsonPath("status").value(202))
                .andExpect(jsonPath("data.accountTransferId").value(1L))
                .andExpect(jsonPath("data.transferMemo").value("test memo 입니다."))
                .andDo(print());
    }

    @Test
    @DisplayName("query 값이 없어서 계좌이체 메모 삭제 실패")
    @WithCustomMockUser
    void 계좌이체메모삭제_실패() throws Exception {
        // given
        String url = "/admin/donation/transfer/detail";
        // when
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.delete(url)
                .contentType(MediaType.APPLICATION_JSON)
//                .queryParam("accountTransferId", String.valueOf(accountTransferId))
        );
        // then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("success").value(false))
                .andExpect(jsonPath("status").value(400))
                .andExpect(jsonPath("message").value("accountTransferId 값이 없습니다."));
    }

    @Test
    @DisplayName("계좌이체메모 삭제 성공")
    @WithCustomMockUser
    void 계좌이체메모삭제_성공() throws Exception {
        // given
        String url = "/admin/donation/transfer/detail";
        Long accountTransferId = 1L;
        doNothing().when(donationService).deleteAccountTransferMemo(1L, accountTransferId);
        // when
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.delete(url)
                .contentType(MediaType.APPLICATION_JSON)
                .queryParam("accountTransferId", String.valueOf(accountTransferId)));
        // then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("success").value(true))
                .andExpect(jsonPath("status").value(202))
                .andExpect(jsonPath("data").isEmpty())
                .andDo(print());
    }

}


















