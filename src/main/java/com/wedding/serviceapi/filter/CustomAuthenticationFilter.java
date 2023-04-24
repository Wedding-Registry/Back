package com.wedding.serviceapi.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wedding.serviceapi.auth.AuthUser;
import com.wedding.serviceapi.auth.dto.LoginSuccessDto;
import com.wedding.serviceapi.auth.jwtutil.JwtUtil;
import com.wedding.serviceapi.auth.vo.LoginRequestVo;
import com.wedding.serviceapi.auth.vo.ServiceLoginRequestVo;
import com.wedding.serviceapi.auth.vo.SocialLoginRequestVo;
import com.wedding.serviceapi.common.vo.ErrorResponseVo;
import com.wedding.serviceapi.common.vo.ResponseVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

@Slf4j
public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final JwtUtil jwtUtil;
    private final ObjectMapper objectMapper;

    public CustomAuthenticationFilter(JwtUtil jwtUtil, ObjectMapper objectMapper) {
        this.jwtUtil = jwtUtil;
        this.objectMapper = objectMapper;
    }

    private final String SERVICE_LOGIN_URI = "/login/service";
    private final String SOCIAL_LOGIN_URI = "/login/oauth";

    private final Map<String, Class> loginUriMap = Map.of(
            SERVICE_LOGIN_URI, ServiceLoginRequestVo.class,
            SOCIAL_LOGIN_URI, SocialLoginRequestVo.class
    );

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        String requestURI = request.getRequestURI();

        log.info("requestURI = {}", requestURI);

        UsernamePasswordAuthenticationToken authenticationToken = loginUriMap.entrySet().stream()
                .filter(uriMap -> requestURI.startsWith(uriMap.getKey()))
                .map(uriMap -> {
                    try {
                        return (LoginRequestVo) objectMapper.readValue(request.getInputStream(), uriMap.getValue());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }).findFirst().map(this::makeAuthenticationToken).get();

        return this.getAuthenticationManager().authenticate(authenticationToken);
    }

    private UsernamePasswordAuthenticationToken makeAuthenticationToken(LoginRequestVo loginRequestVo) {
        return new UsernamePasswordAuthenticationToken(loginRequestVo.getSocialIdOrEmail(), loginRequestVo.getPassword());
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        AuthUser authenticatedUser = (AuthUser) authResult.getPrincipal();
        log.info("인증 성공 필터");
        log.info("authenticatedUser = {}", authenticatedUser);
        // token 발행
        if (authenticatedUser == null) {
            LoginSuccessDto data = new LoginSuccessDto(null, null, null, null, true);
            String responseBody = objectMapper.writeValueAsString(new ResponseVo<>(true, HttpStatus.OK.value(), data));
            setResponseBody(response, responseBody);
        } else {
            Long userId = authenticatedUser.getUserId();
            String userName = authenticatedUser.getUserName();

            ArrayList<String> tokenList = jwtUtil.makeAccessTokenAndRefreshToken(userId, userName);

            LoginSuccessDto data = new LoginSuccessDto(userId, userName, tokenList.get(0), tokenList.get(1), false);
            String responseBody = objectMapper.writeValueAsString(new ResponseVo<>(true, HttpStatus.OK.value(), data));
            log.info("responseBody = {}", responseBody);
            setResponseBody(response, responseBody);
        }
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        log.error("AuthenticationException ", failed);
        String responseBody = objectMapper.writeValueAsString(new ErrorResponseVo(false, HttpStatus.BAD_REQUEST.value(), failed.getMessage()));
        setResponseBody(response, responseBody);
    }

    private void setResponseBody(HttpServletResponse response, String responseBody) throws IOException {
        response.getWriter().write(responseBody);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    }
}












