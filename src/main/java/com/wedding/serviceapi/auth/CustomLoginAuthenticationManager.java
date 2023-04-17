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

    private final List<AuthenticationProvider> providers;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        AuthenticationProvider authenticationProvider;

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
