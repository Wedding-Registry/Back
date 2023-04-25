package com.wedding.serviceapi.auth.service;

import com.wedding.serviceapi.auth.dto.LoginSuccessDto;
import com.wedding.serviceapi.auth.jwtutil.JwtUtil;
import com.wedding.serviceapi.auth.vo.SocialLoginRegisterMoreInfoRequestVo;
import com.wedding.serviceapi.exception.AlreadyExistedUserException;
import com.wedding.serviceapi.exception.NotSamePasswordException;
import com.wedding.serviceapi.users.domain.LoginType;
import com.wedding.serviceapi.users.domain.Role;
import com.wedding.serviceapi.users.domain.Users;
import com.wedding.serviceapi.users.repository.UsersRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
    @Mock
    PasswordEncoder passwordEncoder;
    
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
    @DisplayName("이미 존재하는 이메일인 경우 실패")
    void alreadyExistedEmail() {
        // given
        String name = "test";
        String email = "test";
        String password = "password";
        doReturn(Optional.of(Users.builder().name(name).email(email).build()))
                .when(usersRepository).findByEmail(anyString());
        // when
        AlreadyExistedUserException result = assertThrows(AlreadyExistedUserException.class, () -> authService.registerUser(name, email, password, password, true));
        // then
        assertThat(result.getMessage()).isEqualTo("이미 존재하는 이메일입니다.");
    }

    @Test
    @DisplayName("회원가입 성공")
    void registerUserSuccess() {
        // given
        Users user = Users.builder().id(100L).name("user name").email("email test").password("password").loginType(LoginType.KAKAO).role(Role.USER).build();
        doReturn(user).when(usersRepository).save(any(Users.class));
        doReturn(new ArrayList<>(List.of("accessToken", "refreshToken"))).when(jwtUtil).makeAccessTokenAndRefreshToken(100L, "user name", Role.USER);
        doReturn("encodedPassword").when(passwordEncoder).encode("password");
        // when
        LoginSuccessDto result = authService.registerUser("user name", "email test", "password", "password", true);
        // then
        assertThat(result.getUserId()).isEqualTo(100);
        assertThat(result.getUserName()).isEqualTo("user name");
        assertThat(result.getAccessToken()).isNotNull();
        assertThat(result.getRefreshToken()).isNotNull();

        verify(usersRepository, times(1)).findByEmail("email test");
        verify(usersRepository, times(1)).save(any(Users.class));
    }

    @Test
    @DisplayName("소셜로그인 추가정보 입력 시 password를 갖는 사용자가 이미 존재하는 경우")
    void noNameParamWithSocialMoreInfoRequest() {
        // given
        String password = "socialId";
        String name = "social name";
        String email = "social@test.com";
        doReturn(Optional.of(new Users())).when(usersRepository).findByPassword(password);
        // when
        AlreadyExistedUserException exception = assertThrows(AlreadyExistedUserException.class, () -> authService.registerSocialUser(email, name, password, true));
        // then
        assertThat(exception.getMessage()).isEqualTo("이미 존재하는 사용자입니다.");
    }

    @Test
    @DisplayName("소셜로그인 추가정보 입력 성공 및 로그인 완료")
    void successRegisterSocialUser() {
        // given
        String password = "k1234";
        String name = "social name";
        String email = "social@test.com";
        boolean notification = true;
        Users user = Users.builder().id(100L).name(name).role(Role.USER).build();
        doReturn(user).when(usersRepository).save(any(Users.class));
        doReturn(new ArrayList<>(List.of("accessToken", "refreshToken"))).when(jwtUtil).makeAccessTokenAndRefreshToken(user.getId(), name, Role.USER);
        // when
        LoginSuccessDto loginSuccessDto = authService.registerSocialUser(email, name, password, notification);
        // then
        assertThat(loginSuccessDto.getUserId()).isEqualTo(100L);
        assertThat(loginSuccessDto.getUserName()).isEqualTo("social name");
        assertThat(loginSuccessDto.getAccessToken()).isEqualTo("accessToken");
        assertThat(loginSuccessDto.getRefreshToken()).isEqualTo("refreshToken");
        assertThat(loginSuccessDto.getNeedMoreInfo()).isEqualTo(false);
    }
}


















