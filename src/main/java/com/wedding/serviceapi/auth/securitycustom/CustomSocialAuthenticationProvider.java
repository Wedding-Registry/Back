package com.wedding.serviceapi.auth.securitycustom;

import com.wedding.serviceapi.auth.service.CustomSocialLoginUserDetails;
import com.wedding.serviceapi.exception.InvalidSocialPasswordException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

@RequiredArgsConstructor
@Slf4j
public class CustomSocialAuthenticationProvider implements AuthenticationProvider {

    private final CustomSocialLoginUserDetails customSocialLoginUserDetails;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String email = authentication.getName();
        String password = authentication.getCredentials().toString();

        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("잘못된 이메일입니다.");
        }
        if (!isValidSocialPassword(password)) {
            throw new InvalidSocialPasswordException("유효하지 않는 소셜 비밀번호 입니다.");
        }

        AuthUser authUser = (AuthUser) customSocialLoginUserDetails.loadUserByUsername(email);
        if (authUser == null) {
            log.info("최초 소셜 로그인 사용자");
            return new UsernamePasswordAuthenticationToken(null, null);
        } else {
            log.info("이미 소셜 로그인한 경우가 있는 사용자");
            return new UsernamePasswordAuthenticationToken(authUser, null);
        }
    }

    // TODO: 2023/04/18 소셜 추가정보 입력 부분과 중복 제거 가능여부 확인
    private boolean isValidSocialPassword(String password) {
        return password != null || !password.isBlank() || password.toLowerCase().startsWith("k") || password.toLowerCase().startsWith("g") ;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return false;
    }
}
