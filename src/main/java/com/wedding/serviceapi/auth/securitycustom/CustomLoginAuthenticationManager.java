package com.wedding.serviceapi.auth.securitycustom;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import java.util.Map;

@RequiredArgsConstructor
@Slf4j
public class CustomLoginAuthenticationManager implements AuthenticationManager {
    private final Map<String, AuthenticationProvider> providerMap;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String requestURI = (String) RequestContextHolder.currentRequestAttributes().getAttribute("requestURI", RequestAttributes.SCOPE_SESSION);
        log.info("AuthenticationManager requestURI = {}", requestURI);
        AuthenticationProvider authenticationProvider = providerMap.get(requestURI);

        return authenticationProvider.authenticate(authentication);
    }
}