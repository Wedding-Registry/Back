package com.wedding.serviceapi;

import com.wedding.serviceapi.auth.securitycustom.AuthUser;
import com.wedding.serviceapi.users.domain.Role;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import java.util.ArrayList;
import java.util.List;

public class MockSecurityContextFactory implements WithSecurityContextFactory<WithCustomMockUser> {

    @Override
    public SecurityContext createSecurityContext(WithCustomMockUser annotation) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                new AuthUser(1L, annotation.username(), annotation.boardsId(),
                        Role.USER), null,
                new ArrayList<>(List.of(new SimpleGrantedAuthority(Role.USER.name()))));

        context.setAuthentication(authentication);
        return context;
    }
}
