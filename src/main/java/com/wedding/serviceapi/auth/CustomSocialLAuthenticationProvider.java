package com.wedding.serviceapi.auth;

import com.wedding.serviceapi.auth.service.CustomSocialLoginUserDetails;
import com.wedding.serviceapi.exception.InvalidSocialIdException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;

@RequiredArgsConstructor
@Slf4j
public class CustomSocialLAuthenticationProvider implements AuthenticationProvider {

    private final CustomSocialLoginUserDetails customSocialLoginUserDetails;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String socialId = authentication.getName();
        if (!isValidSocialId(socialId)) {
            throw new InvalidSocialIdException("유효하지 않는 소셜아이디 입니다.");
        }

        AuthUser authUser = (AuthUser) customSocialLoginUserDetails.loadUserByUsername(socialId);
        if (authUser == null) {
            log.info("최초 소셜 로그인 사용자");
            return new UsernamePasswordAuthenticationToken(null, null);
        } else {
            log.info("이미 소셜 로그인한 경우가 있는 사용자");
            return new UsernamePasswordAuthenticationToken(authUser, null);
        }
    }

    private boolean isValidSocialId(String socialId) {
        return socialId.toLowerCase().startsWith("k") || socialId.toLowerCase().startsWith("g");
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return false;
    }
}
