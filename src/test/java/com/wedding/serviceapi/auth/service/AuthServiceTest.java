package com.wedding.serviceapi.auth.service;

import com.wedding.serviceapi.auth.dto.LoginSuccessDto;
import com.wedding.serviceapi.auth.jwtutil.JwtUtil;
import com.wedding.serviceapi.exception.AlreadyExistedUserException;
import com.wedding.serviceapi.exception.NotSamePasswordException;
import com.wedding.serviceapi.users.domain.LoginType;
import com.wedding.serviceapi.users.domain.Users;
import com.wedding.serviceapi.users.repository.UsersRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@Slf4j
class AuthServiceTest {

    @InjectMocks
    AuthService authService;
    
    @Mock
    UsersRepository usersRepository;

    @Mock
    JwtUtil jwtUtil;
    
    @Test
    @DisplayName("입력받은 비밀번호가 서로 다른 경우")
    void passwordCheckFail() {
        // given
        String password = "password";
        String passwordCheck = "password2";
        // when
        NotSamePasswordException result  = assertThrows(NotSamePasswordException.class, () -> authService.registerUser("name", "email", password, passwordCheck, true));
        // then
        assertThat(result.getMessage()).isEqualTo("비밀번호가 서로 다릅니다.");
    }

    @Test
    @DisplayName("이미 가입한 유저인 경우 실패")
    void alreadyRegisteredUser() {
        // given
        String name = "test";
        String email = "test";
        String password = "password";
        doReturn(Optional.of(Users.builder().name(name).email(email).build()))
                .when(usersRepository).findByNameAndEmail(anyString(), anyString());
        // when
        AlreadyExistedUserException result = assertThrows(AlreadyExistedUserException.class, () -> authService.registerUser(name, email, password, password, true));
        // then
        assertThat(result.getMessage()).isEqualTo("이미 존재하는 사용자 입니다.");
    }

    @Test
    @DisplayName("회원가입 성공")
    void registerUserSuccess() {
        // given
        Users user = Users.builder().id(100L).name("user name").email("email test").password("password").loginType(LoginType.KAKAO).build();
        doReturn(user).when(usersRepository).save(any(Users.class));
        doReturn("accessToken").when(jwtUtil).makeAccessToken(100L, "user name");
        doReturn("refreshToken").when(jwtUtil).makeRefreshToken(100L, "user name");
        // when
        LoginSuccessDto result = authService.registerUser("user name", "email test", "password", "password", true);
        // then
        assertThat(result.getUserId()).isEqualTo(100);
        assertThat(result.getUserName()).isEqualTo("user name");
        assertThat(result.getAccessToken()).isNotNull();
        assertThat(result.getRefreshToken()).isNotNull();

        verify(usersRepository, times(1)).findByNameAndEmail("user name", "email test");
        verify(usersRepository, times(1)).save(any(Users.class));
    }


    
}


















