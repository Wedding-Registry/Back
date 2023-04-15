package com.wedding.serviceapi.auth;

import com.wedding.serviceapi.auth.service.CustomServiceLoginUserDetails;
import com.wedding.serviceapi.users.domain.Users;
import com.wedding.serviceapi.users.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.NoSuchElementException;

@RequiredArgsConstructor
public class CustomServiceLoginAuthenticationManager implements AuthenticationManager {

    // TODO: 2023/04/16 이후 jwt 로그인과 통합 가능하기 때문에 수정 예정
    private final CustomServiceAuthenticationProvider customServiceAuthenticationProvider;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        Authentication authenticate = customServiceAuthenticationProvider.authenticate(authentication);
        return authenticate;
    }
}
