package com.wedding.serviceapi.auth.service;

import com.wedding.serviceapi.auth.AuthUser;
import com.wedding.serviceapi.users.domain.Users;
import com.wedding.serviceapi.users.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@RequiredArgsConstructor
public class CustomSocialLoginUserDetails implements UserDetailsService {

    private final UsersRepository usersRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Users users = usersRepository.findBySocialId(username).orElse(null);
        // TODO: 2023/04/16 2023/04/16 권한 추가는 추가로 여기서 AuthUser 생성시 진행하면 된다.
        if (users == null) {
            return null;
        }
        return new AuthUser(users);
    }
}
