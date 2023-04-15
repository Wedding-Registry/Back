package com.wedding.serviceapi.auth.service;

import com.wedding.serviceapi.auth.dto.LoginSuccessDto;
import com.wedding.serviceapi.auth.jwtutil.JwtUtil;
import com.wedding.serviceapi.exception.AlreadyExistedUserException;
import com.wedding.serviceapi.exception.NotSamePasswordException;
import com.wedding.serviceapi.users.domain.LoginType;
import com.wedding.serviceapi.users.domain.Users;
import com.wedding.serviceapi.users.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {

    private final UsersRepository usersRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    public LoginSuccessDto registerUser(String name, String email, String password, String passwordCheck, boolean notification) {

        validatePassword(password, passwordCheck);
        validateIfExistEmail(email);

        Users user = Users.builder()
                .name(name)
                .email(email)
                .password(passwordEncoder.encode(password))
                .alarmEvent(notification)
                .loginType(LoginType.SERVICE)
                .build();

        Users savedUser = usersRepository.save(user);
        ArrayList<String> tokenList = jwtUtil.makeAccessTokenAndRefreshToken(savedUser.getId(), savedUser.getName());
        savedUser.setRefreshToken(tokenList.get(1));

        return new LoginSuccessDto(savedUser.getId(), savedUser.getName(), tokenList.get(0), tokenList.get(1));
    }

    private void validatePassword(String password, String passwordCheck) {
        // TODO: 2023/04/12 비밀번호 규칙 체크 필요
        if (!password.equals(passwordCheck)) {
            throw new NotSamePasswordException("비밀번호가 서로 다릅니다.");
        }
    }

    private void validateIfExistEmail(String email) {
        Users userByEmail = usersRepository.findByEmail(email).orElse(null);
        if (userByEmail != null) {
            throw new IllegalArgumentException("이미 존재하는 이메일 입니다.");
        }
    }
}
