package com.wedding.serviceapi.auth.service;

import com.wedding.serviceapi.auth.securitycustom.AuthUser;
import com.wedding.serviceapi.boards.domain.Boards;
import com.wedding.serviceapi.boards.repository.BoardsRepository;
import com.wedding.serviceapi.exception.NoSuchUserException;
import com.wedding.serviceapi.users.domain.Users;
import com.wedding.serviceapi.users.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
public class CustomServiceLoginUserDetails implements UserDetailsService {

    private final UsersRepository usersRepository;
    private final BoardsRepository boardsRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Users users = usersRepository.findByEmail(username).orElseThrow(() -> new NoSuchUserException("이메일이 틀렸습니다."));
        Optional<Boards> optionalBoards = boardsRepository.findByUsersIdNotDeleted(users.getId());
        log.info("user id = {} email = {} name = {}", users.getId(), users.getEmail(), users.getName());

        if (optionalBoards.isPresent()) {
            return new AuthUser(users, optionalBoards.get().getId());
        } else {
            return new AuthUser(users, null);
        }
    }
}
