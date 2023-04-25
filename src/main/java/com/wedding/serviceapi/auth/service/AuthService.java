package com.wedding.serviceapi.auth.service;

import com.wedding.serviceapi.auth.dto.LoginSuccessDto;
import com.wedding.serviceapi.auth.jwtutil.JwtUtil;
import com.wedding.serviceapi.exception.AlreadyExistedUserException;
import com.wedding.serviceapi.exception.InvalidSocialPasswordException;
import com.wedding.serviceapi.exception.NotSamePasswordException;
import com.wedding.serviceapi.users.domain.LoginType;
import com.wedding.serviceapi.users.domain.Role;
import com.wedding.serviceapi.users.domain.Users;
import com.wedding.serviceapi.users.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {

    private final UsersRepository usersRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    public LoginSuccessDto registerUser(String name, String email, String password, String passwordCheck, boolean notification) {
        validatePassword(password, passwordCheck);
        checkIfExistEmail(email);

        Users user = Users.builder()
                .name(name)
                .email(email)
                .password(passwordEncoder.encode(password))
                .alarmEvent(notification)
                .loginType(LoginType.SERVICE)
                .role(Role.USER)
                .build();

        Users savedUser = usersRepository.save(user);
        ArrayList<String> tokenList = jwtUtil.makeAccessTokenAndRefreshToken(savedUser.getId(), savedUser.getName(), savedUser.getRole());
        savedUser.setRefreshToken(tokenList.get(1));

        return new LoginSuccessDto(savedUser.getId(), savedUser.getName(), tokenList.get(0), tokenList.get(1), false);
    }

    private void validatePassword(String password, String passwordCheck) {
        // TODO: 2023/04/12 비밀번호 규칙 체크 필요
        if (!password.equals(passwordCheck)) {
            throw new NotSamePasswordException("비밀번호가 서로 다릅니다.");
        }
    }

    private void checkIfExistEmail(String email) {
        Users userByEmail = usersRepository.findByEmail(email).orElse(null);
        if (userByEmail != null) {
            throw new AlreadyExistedUserException("이미 존재하는 이메일입니다.");
        }
    }

    public LoginSuccessDto registerSocialUser(String email, String name, String password, boolean notification) {
        Users userBySocialId = usersRepository.findByPassword(password).orElse(null);
        if (userBySocialId != null) {
            throw new AlreadyExistedUserException("이미 존재하는 사용자입니다.");
        }

        if (!isValidSocialLoginPassword(password)) {
            throw new InvalidSocialPasswordException("유효하지 않는 소셜아이디 입니다.");
        }

        // TODO: 2023/04/17 기존에 서비스 회원가입을 통해 이메일이 존재하고 다시 소셜 회원가입을 하느라 이메일이 겹치는 경우 해결 필요
        LoginType loginType = password.startsWith("k") ? LoginType.KAKAO : LoginType.GOOGLE;
        Users user = Users.builder()
                .name(name)
                .email(email)
                .password(password)
                .loginType(loginType)
                .alarmEvent(notification)
                .role(Role.USER)
                .build();

        Users savedUser = usersRepository.save(user);
        ArrayList<String> tokenList = jwtUtil.makeAccessTokenAndRefreshToken(savedUser.getId(), savedUser.getName(), savedUser.getRole());
        savedUser.setRefreshToken(tokenList.get(1));

        return new LoginSuccessDto(savedUser.getId(), savedUser.getName(), tokenList.get(0), tokenList.get(1), false);
    }

    private boolean isValidSocialLoginPassword(String password) {
        return password.toLowerCase().startsWith("k") || password.toLowerCase().startsWith("g");
    }
}























