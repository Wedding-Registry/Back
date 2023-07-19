package com.wedding.serviceapi.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wedding.serviceapi.auth.securitycustom.AuthUser;
import com.wedding.serviceapi.common.jwtutil.AuthJwtUtil;
import com.wedding.serviceapi.common.vo.ErrorResponseVo;
import com.wedding.serviceapi.common.vo.LoginUserInfoVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class CustomAuthorizationFilter extends OncePerRequestFilter {

    private final AuthJwtUtil jwtUtil;
    private final ObjectMapper objectMapper;

    private final List<String> URL_MATCHER = new ArrayList<>(
            List.of(
                    "/auth/signup",
                    "/login/service",
                    "/login/oauth",
                    "/auth/social/info",
                    "/prod/hello",
                    "/actuator"
            )
    );

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String requestURI = request.getRequestURI();
        // url이 회원가입, 로그인인 경우 작동하면 안된다.
        for (String uri : URL_MATCHER) {
            if (requestURI.startsWith(uri)) {
                filterChain.doFilter(request, response);
                return;
            }
        }

        String bearerToken = request.getHeader(HttpHeaders.AUTHORIZATION);
        log.info("bearerToken = {}", bearerToken);

        if (bearerToken != null) {
            try {
                LoginUserInfoVo loginUserInfoVo = jwtUtil.decodeJwt(bearerToken);
                log.info("login role = {}", loginUserInfoVo.getRole().name());
                Authentication authentication = new UsernamePasswordAuthenticationToken(
                        new AuthUser(loginUserInfoVo.getUserId(), loginUserInfoVo.getUserName(), loginUserInfoVo.getBoardsId(),
                                loginUserInfoVo.getRole()), null,
                        new ArrayList<>(List.of(new SimpleGrantedAuthority(loginUserInfoVo.getRole().name()))));

                SecurityContextHolder.getContext().setAuthentication(authentication);
                filterChain.doFilter(request, response);

            } catch (IllegalArgumentException e1) {
                log.error("jwtAuthenticationFilter error");
                // jwt 토큰이 문제인 경우
                ErrorResponseVo body = new ErrorResponseVo(false, HttpStatus.UNAUTHORIZED.value(), "유효한 토큰이 아닙니다.");
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                response.getWriter().write(objectMapper.writeValueAsString(body));
            }
        } else {
            ErrorResponseVo body = new ErrorResponseVo(false, HttpStatus.FORBIDDEN.value(), "인증 토큰이 필요합니다.");
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.getWriter().write(objectMapper.writeValueAsString(body));
        }
    }
}
