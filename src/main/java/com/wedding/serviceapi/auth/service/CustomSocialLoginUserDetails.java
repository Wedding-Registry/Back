package com.wedding.serviceapi.auth.service;

import com.wedding.serviceapi.auth.securitycustom.AuthUser;
import com.wedding.serviceapi.boards.domain.Boards;
import com.wedding.serviceapi.boards.repository.BoardsRepository;
import com.wedding.serviceapi.users.domain.Users;
import com.wedding.serviceapi.users.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

@RequiredArgsConstructor
public class CustomSocialLoginUserDetails implements UserDetailsService {

    private final UsersRepository usersRepository;
    private final BoardsRepository boardsRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Users users = usersRepository.findByEmail(username).orElse(null);
        // TODO: 2023/04/25 기존 서비스로 가입을 한 이메일과 겹치는 경우 어떻게 해결할 것인가...
        if (users == null) {
            return null;
        }

        Optional<Boards> optionalBoards = boardsRepository.findByUsersIdNotDeleted(users.getId());
        if (optionalBoards.isPresent()) {
            return new AuthUser(users, optionalBoards.get().getId());
        } else {
            return new AuthUser(users, null);
        }
    }
}
