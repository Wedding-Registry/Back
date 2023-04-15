package com.wedding.serviceapi.auth;

import com.wedding.serviceapi.auth.service.CustomServiceLoginUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

@RequiredArgsConstructor
public class CustomServiceAuthenticationProvider implements AuthenticationProvider {

    private final CustomServiceLoginUserDetails customServiceLoginUserDetails;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String email = authentication.getName();
        String password = authentication.getCredentials().toString();

        AuthUser authUser = (AuthUser) customServiceLoginUserDetails.loadUserByUsername(email);
        String encodedPassword = authUser.getPassword();
        boolean isValidPassword = passwordEncoder.matches(password, encodedPassword);
        if (!isValidPassword) throw new IllegalArgumentException("잘못된 비밀번호입니다.");

        UsernamePasswordAuthenticationToken result = new UsernamePasswordAuthenticationToken(authUser, null);
        return result;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return false;
    }
}
