package com.wedding.serviceapi.boards.repository;

import com.wedding.serviceapi.users.domain.LoginType;
import com.wedding.serviceapi.users.domain.Users;
import com.wedding.serviceapi.users.repository.UsersRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import javax.persistence.EntityManager;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class BoardsRepositoryTest {

    @Autowired
    UsersRepository usersRepository;
    @Autowired
    EntityManager em;

    @Test
    @DisplayName("유저아이디를 이용해 신랑 신부 유저 찾기")
    void findUserOrParentUser() {
        // given
        Users parent = new Users("parent", "parent", LoginType.KAKAO);
        Users child = Users.builder().email("child").parent(parent).password("child").loginType(LoginType.KAKAO).build();
        usersRepository.save(parent);
        usersRepository.save(child);
        em.flush();
        em.clear();

        // when
//        usersRepository

    }
}


















