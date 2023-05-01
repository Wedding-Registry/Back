package com.wedding.serviceapi.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wedding.serviceapi.auth.securitycustom.AuthUser;
import com.wedding.serviceapi.auth.dto.LoginSuccessDto;
import com.wedding.serviceapi.auth.jwtutil.JwtUtil;
import com.wedding.serviceapi.auth.vo.LoginRequestVo;
import com.wedding.serviceapi.common.vo.ErrorResponseVo;
import com.wedding.serviceapi.common.vo.ResponseVo;
import com.wedding.serviceapi.users.domain.Role;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

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


    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        LoginRequestVo loginRequestVo;
        String requestURI = request.getRequestURI();

        log.info("requestURI = {}", requestURI);

        try {
            loginRequestVo = objectMapper.readValue(request.getInputStream(), LoginRequestVo.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        UsernamePasswordAuthenticationToken authenticationToken = makeAuthenticationToken(loginRequestVo);

        RequestContextHolder.currentRequestAttributes().setAttribute("requestURI", requestURI, RequestAttributes.SCOPE_SESSION);

        return this.getAuthenticationManager().authenticate(authenticationToken);
    }

    private UsernamePasswordAuthenticationToken makeAuthenticationToken(LoginRequestVo loginRequestVo) {
        return new UsernamePasswordAuthenticationToken(loginRequestVo.getEmail(), loginRequestVo.getPassword());
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        AuthUser authenticatedUser = (AuthUser) authResult.getPrincipal();
        log.info("인증 성공 필터");
        log.info("authenticatedUser = {}", authenticatedUser);
        // token 발행
        if (authenticatedUser == null) {
            LoginSuccessDto data = new LoginSuccessDto(null, null, null, null, null, true);
            String responseBody = objectMapper.writeValueAsString(new ResponseVo<>(true, HttpStatus.OK.value(), data));
            setResponseBody(response, responseBody);
        } else {
            Long userId = authenticatedUser.getUserId();
            String userName = authenticatedUser.getUserName();
            Long boardsId = authenticatedUser.getBoardsId();
            Role role = authenticatedUser.getUsers().getRole();

            ArrayList<String> tokenList = jwtUtil.makeAccessTokenAndRefreshToken(userId, userName, boardsId, role);

            LoginSuccessDto data = new LoginSuccessDto(userId, userName, boardsId, tokenList.get(0), tokenList.get(1), false);
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












