package com.wedding.serviceapi.auth.service;

import com.wedding.serviceapi.auth.AuthUser;
import com.wedding.serviceapi.exception.NoSuchUserException;
import com.wedding.serviceapi.users.domain.Users;
import com.wedding.serviceapi.users.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
public class CustomServiceLoginUserDetails implements UserDetailsService {

    private final UsersRepository usersRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Users users = usersRepository.findByEmail(username).orElseThrow(() -> new NoSuchUserException("이메일이 틀렸습니다."));
        log.info("user id = {} email = {} name = {}", users.getId(), users.getEmail(), users.getName());
        // TODO: 2023/04/16 권한 추가는 추가로 여기서 AuthUser 생성시 진행하면 된다.
        return new AuthUser(users);
    }
}
