package com.wedding.serviceapi.users.repository;

import com.wedding.serviceapi.users.domain.LoginType;
import com.wedding.serviceapi.users.domain.Users;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Slf4j
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UsersRepositoryTest {

    @Autowired
    UsersRepository usersRepository;

    @Autowired
    EntityManager em;

    @Test
    @DisplayName("유저 이름과 이메일을 통해 유저를 찾지 못한 경우 실패")
    void findByEmailAndNameFail() {
        // given
        String name = "test name";
        String email = "test email";
        // when
        assertThrows(NoSuchElementException.class, () -> usersRepository.findByNameAndEmail(name, email)
                .orElseThrow(() -> new NoSuchElementException("해당하는 유저가 없습니다.")));
    }

    @Test
    @DisplayName("유저 이름과 이메일을 통해 유저를 찾은 경우 성공")
    void findByEmailAndNameSuccess() {
        // given
        registerUser();
        // when
        Users users = usersRepository.findByNameAndEmail("test", "test email").get();
        // then
        assertThat(users.getName()).isEqualTo("test");
        assertThat(users.getEmail()).isEqualTo("test email");
    }

    @Test
    @DisplayName("유저 이름만 같은 경우 유저 찾기 실패")
    void findByNameAndEmailOnlyMatchName() {
        // given
        registerUser();
        // when
        assertThrows(NoSuchElementException.class, () -> usersRepository.findByNameAndEmail("test", "email")
                .orElseThrow(() -> new NoSuchElementException("해당하는 유저가 없습니다.")));

    }

    @Test
    @DisplayName("유저 이메일만 같은 경우 유저 찾기 실패")
    void findByNameAndEmailOnlyMatchEmail() {
        // given
        registerUser();
        // when
        assertThrows(NoSuchElementException.class, () -> usersRepository.findByNameAndEmail("test name", "test email")
                .orElseThrow(() -> new NoSuchElementException("해당하는 유저가 없습니다.")));
    }

    @Test
    @DisplayName("소셜 아이디를 통해 사용자 찾기 실패")
    void findBySocialIFail() {
        // given
        Users user = Users.builder().email("test email").name("test").loginType(LoginType.KAKAO).password("K1234").build();
        usersRepository.save(user);
        em.flush();
        em.clear();
        // when
        assertThrows(NoSuchElementException.class, () -> usersRepository.findByPassword("k12345")
                .orElseThrow(() -> new NoSuchElementException("해당하는 유저가 없습니다.")));
    }

    @Test
    @DisplayName("소셜 아이디를 통해 사용자 찾기 성공")
    void findByPassword() {
        // given
        Users user = Users.builder().email("test email").name("test").loginType(LoginType.KAKAO).password("K1234").build();
        usersRepository.save(user);
        em.flush();
        em.clear();
        // when
        Users users = usersRepository.findByPassword("k1234").get();
        assertThat(users.getEmail()).isEqualTo("test email");
        assertThat(users.getName()).isEqualTo("test");
        assertThat(users.getLoginType()).isEqualTo(LoginType.KAKAO);
        assertThat(users.getPassword()).isEqualToIgnoringCase("k1234");
    }

    @Test
    @DisplayName("in쿼리 작동 확인 테스트")
    void in쿼리_사용하기() {
        // given
        Users user1 = Users.builder().email("test email").name("test1").password("test1").loginType(LoginType.KAKAO).build();
        Users user2 = Users.builder().email("test email").name("test1").password("test1").loginType(LoginType.KAKAO).build();
        Users user3 = Users.builder().email("test email").name("test1").password("test1").loginType(LoginType.KAKAO).build();
        List<Users> users = usersRepository.saveAllAndFlush(List.of(user1, user2, user3));
        List<Long> usersIdList = users.stream().map(Users::getId).collect(Collectors.toList());

        // when
        List<Users> foundUserList = usersRepository.findByIdIn(usersIdList);
        // then
        assertThat(foundUserList.size()).isEqualTo(3);
    }

    @Test
    @DisplayName("list 형태를 map으로 변환하는 테스트")
    void map변환_테스트() {
        // given
        Users user1 = Users.builder().email("test1 email").name("test1").password("test1").loginType(LoginType.KAKAO).build();
        Users user2 = Users.builder().email("test2 email").name("test2").password("test1").loginType(LoginType.KAKAO).build();
        Users user3 = Users.builder().email("test3 email").name("test3").password("test1").loginType(LoginType.KAKAO).build();
        List<Users> users = usersRepository.saveAllAndFlush(List.of(user1, user2, user3));
        List<Long> usersIdList = users.stream().map(Users::getId).collect(Collectors.toList());
        // when
        Map<Long, Users> resultMap = usersRepository.findByIdInByMap(usersIdList);
        // then
        assertThat(resultMap.size()).isEqualTo(3);
        assertThat(resultMap.get(2L).getName()).isEqualTo("test2");
    }

    private void registerUser() {
        Users user = Users.builder().email("test email").name("test").password("test").loginType(LoginType.KAKAO).build();
        usersRepository.save(user);
        em.flush();
        em.clear();
    }
}










