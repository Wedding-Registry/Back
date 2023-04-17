package com.wedding.serviceapi.auth;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
public class CustomLoginAuthenticationManager implements AuthenticationManager {

    // TODO: 2023/04/16 이후 jwt 로그인과 통합 가능하기 때문에 수정 예정
    private final List<AuthenticationProvider> providers;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        AuthenticationProvider authenticationProvider;

        // TODO: 2023/04/16 AuthenticationProvider의 support 메서드를 이용해서 원하는 provider를 선택해야한다.
        log.info("credentials = {}", authentication.getCredentials());
        if (authentication.getCredentials() != null) {
            log.info("service login provider");
            authenticationProvider = providers.get(0);
        } else {
            log.info("social login provider");
            authenticationProvider = providers.get(1);
        }

        return authenticationProvider.authenticate(authentication);
    }
}
