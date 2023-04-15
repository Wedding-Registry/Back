package com.wedding.serviceapi.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wedding.serviceapi.auth.AuthUser;
import com.wedding.serviceapi.auth.CustomServiceLoginAuthenticationManager;
import com.wedding.serviceapi.auth.dto.LoginSuccessDto;
import com.wedding.serviceapi.auth.jwtutil.JwtUtil;
import com.wedding.serviceapi.auth.vo.ServiceLoginRequestVo;
import com.wedding.serviceapi.users.domain.Users;
import com.wedding.serviceapi.users.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;

@Slf4j
public class ServiceLoginFilter extends UsernamePasswordAuthenticationFilter {

    private final JwtUtil jwtUtil;
    private final ObjectMapper objectMapper;

    public ServiceLoginFilter(JwtUtil jwtUtil, ObjectMapper objectMapper) {
        this.jwtUtil = jwtUtil;
        this.objectMapper = objectMapper;

    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        try {
            ServiceLoginRequestVo loginRequestVo = objectMapper.readValue(request.getInputStream(), ServiceLoginRequestVo.class);

            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                    loginRequestVo.getEmail(),
                    loginRequestVo.getPassword()
            );

            return this.getAuthenticationManager().authenticate(authenticationToken);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        AuthUser authenticatedUser = (AuthUser) authResult.getPrincipal();
        
        // token 발행
        Long userId = authenticatedUser.getUserId();
        String userName = authenticatedUser.getUserName();

        String accessToken = jwtUtil.makeAccessToken(userId, userName);
        String refreshToken = jwtUtil.makeRefreshToken(userId, userName);

        String responseBody = objectMapper.writeValueAsString(new LoginSuccessDto(userId, userName, accessToken, refreshToken));
        log.info("responseBody = {}", responseBody);
        response.getWriter().write(responseBody);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    }
}












