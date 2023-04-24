package com.wedding.serviceapi.auth;

import com.wedding.serviceapi.auth.jwtutil.JwtUtil;
import com.wedding.serviceapi.common.vo.LoginUserInfoVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.SignatureException;
import java.util.ArrayList;

@Slf4j
@RequiredArgsConstructor
public class CustomJwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String bearerToken = request.getHeader(HttpHeaders.AUTHORIZATION);
        log.info("bearerToken = {}", bearerToken);

        if (bearerToken != null) {
            try {
                LoginUserInfoVo loginUserInfoVo = jwtUtil.decodeJwt(bearerToken);

                Authentication authentication = new UsernamePasswordAuthenticationToken(new AuthUser(loginUserInfoVo.getUserId(), loginUserInfoVo.getUserName()), null, new ArrayList<>());

                SecurityContextHolder.getContext().setAuthentication(authentication);
            } catch (IllegalArgumentException e1) {
                log.error("jwtAuthenticationFilter error");
            }
        }

        filterChain.doFilter(request, response);
    }
}
