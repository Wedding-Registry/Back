package com.wedding.serviceapi.auth.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wedding.serviceapi.auth.dto.LoginSuccessDto;
import com.wedding.serviceapi.auth.service.AuthService;
import com.wedding.serviceapi.auth.vo.RegisterUserRequestVo;
import com.wedding.serviceapi.common.exceptionhandler.GlobalExceptionHandler;
import com.wedding.serviceapi.exception.AlreadyExistedUserException;
import com.wedding.serviceapi.exception.NotSamePasswordException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {
    
    @InjectMocks
    private AuthController authController;

    @Mock
    private AuthService authService;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    public void init() {
        objectMapper = new ObjectMapper();
        mockMvc = MockMvcBuilders.standaloneSetup(authController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    
    @Test
    @DisplayName("이름 정보가 없이 body에 들어옴")
    void noNameFiledInRequestBody() throws Exception {
        // given
        String url = "/auth/signup";
        RegisterUserRequestVo registerUserRequestVo = new RegisterUserRequestVo(null, "email@test.com", "password", "password", true);
        String body = objectMapper.writeValueAsString(registerUserRequestVo);
        // when
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(body));

        // then
        resultActions.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("success").value(false))
                .andExpect(MockMvcResultMatchers.jsonPath("status").value(400))
                .andExpect(MockMvcResultMatchers.jsonPath("message").value("이름을 작성해 주세요."))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("이메일 정보가 잘못된 형태로 body에 들어옴")
    void noEmailFiledInRequestBody() throws Exception {
        // given
        String url = "/auth/signup";
        RegisterUserRequestVo registerUserRequestVo = new RegisterUserRequestVo("name", "null", "password", "password", true);
        String body = objectMapper.writeValueAsString(registerUserRequestVo);
        // when
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(body));

        // then
        resultActions.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("success").value(false))
                .andExpect(MockMvcResultMatchers.jsonPath("status").value(400))
                .andExpect(MockMvcResultMatchers.jsonPath("message").value("이메일 형식이 아닙니다."))
                .andDo(MockMvcResultHandlers.print());
    }
    
    @Test
    @DisplayName("확인용 패스워드가 일치하지 않는 경우 회원가입 실패")
    void notSamePasswordException() throws Exception {
        // given
        String url = "/auth/signup";
        RegisterUserRequestVo requestBody = new RegisterUserRequestVo("name", "test@naver.com", "password", "password2", true);
        String body = objectMapper.writeValueAsString(requestBody);
        doThrow(new NotSamePasswordException("test message")).when(authService).registerUser("name", "test@naver.com", "password", "password2", true);

        // when
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(body));

        // then
        resultActions.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("success").value(false))
                .andExpect(MockMvcResultMatchers.jsonPath("status").value(400))
                .andExpect(MockMvcResultMatchers.jsonPath("message").value("test message"))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("이미 존재하는 사용자가 있는 경우 회원가입 실패")
    void alreadyExistedUserException() throws Exception {
        // given
        String url = "/auth/signup";
        RegisterUserRequestVo requestBody = new RegisterUserRequestVo("name", "test@naver.com", "password", "password2", true);
        String body = objectMapper.writeValueAsString(requestBody);
        doThrow(new AlreadyExistedUserException("test message")).when(authService).registerUser("name", "test@naver.com", "password", "password2", true);
        // when
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(body));

        // then
        resultActions.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("success").value(false))
                .andExpect(MockMvcResultMatchers.jsonPath("status").value(400))
                .andExpect(MockMvcResultMatchers.jsonPath("message").value("test message"))
                .andDo(MockMvcResultHandlers.print());
    }
    
    @Test
    @DisplayName("사용자 회원가입 성공")
    void successRegisterUser() throws Exception {
        // given
        String url = "/auth/signup";
        RegisterUserRequestVo requestBody = new RegisterUserRequestVo("name", "test@naver.com", "password", "password2", true);
        String body = objectMapper.writeValueAsString(requestBody);
        LoginSuccessDto registeredUser = new LoginSuccessDto(1L, "name", "accessToken", "refreshToken", false);
        doReturn(registeredUser).when(authService).registerUser("name", "test@naver.com", "password", "password2", true);
        // when
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(body));
        // then
        resultActions.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("success").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("status").value(201))
                .andExpect(MockMvcResultMatchers.jsonPath("data.userId").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("data.userName").value("name"))
                .andExpect(MockMvcResultMatchers.jsonPath("data.accessToken").value("accessToken"))
                .andExpect(MockMvcResultMatchers.jsonPath("data.refreshToken").value("refreshToken"))
                .andExpect(MockMvcResultMatchers.jsonPath("data.needMoreInfo").value(false))
                .andDo(MockMvcResultHandlers.print());
    }
}
















