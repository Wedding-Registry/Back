package com.wedding.serviceapi.users.domain;

import com.wedding.serviceapi.users.repository.UsersRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
@Slf4j
class UsersTest {

    @Autowired UsersRepository usersRepository;
    @Autowired EntityManager em;

    @Test
    void usersForeignKeyTest() {
        // given
        Users parent = new Users("parent", "password", LoginType.KAKAO);
        Users child1 = new Users("child1", "password", LoginType.KAKAO);
        Users child2 = new Users("child2", "password", LoginType.KAKAO);

        em.persist(parent);
        em.persist(child1);
        em.persist(child2);

        // when
        child1.setParentId(parent);

        // then
        assertThat(child1.getParent().getId()).isEqualTo(parent.getId());
        assertThat(child2.getParent()).isNull();
    }

    @Test
    void deleteTest() {
        Users parent = new Users("parent", "password", LoginType.KAKAO);

        em.persist(parent);
        log.info("deleted at = {}", parent.getDeletedAt());
        assertThat(parent.getDeletedAt()).isEqualTo(false);
    }

}