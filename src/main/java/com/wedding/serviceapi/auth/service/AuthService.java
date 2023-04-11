package com.wedding.serviceapi.auth.service;

import com.wedding.serviceapi.auth.dto.LoginSuccessDto;
import com.wedding.serviceapi.auth.jwtutil.JwtUtil;
import com.wedding.serviceapi.exception.AlreadyExistedUserException;
import com.wedding.serviceapi.exception.NotSamePasswordException;
import com.wedding.serviceapi.users.domain.Users;
import com.wedding.serviceapi.users.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {

    private final UsersRepository usersRepository;
    private final JwtUtil jwtUtil;

    public LoginSuccessDto registerUser(String name, String email, String password, String passwordCheck, boolean notification) {

        // TODO: 2023/04/12 비밀번호 규칙 체크 필요
        if (!password.equals(passwordCheck)) {
            throw new NotSamePasswordException("비밀번호가 서로 다릅니다.");
        }

        Users users = usersRepository.findByNameAndEmail(name, email).orElse(null);
        if (users != null) {
            throw new AlreadyExistedUserException("이미 존재하는 사용자 입니다.");
        }

        // TODO: 2023/04/12 비밀번호 암호화 필요
        Users user = Users.builder()
                .name(name)
                .email(email)
                .password(password)
                .alarmEvent(notification).build();

        Users savedUser = usersRepository.save(user);
        // TODO: 2023/04/12 토큰 생성 부분 리팩토링 필요할 듯
        String accessToken = jwtUtil.makeAccessToken(savedUser.getId(), savedUser.getName());
        String refreshToken = jwtUtil.makeRefreshToken(savedUser.getId(), savedUser.getName());

        return new LoginSuccessDto(savedUser.getId(), savedUser.getName(), accessToken, refreshToken);

    }
}
