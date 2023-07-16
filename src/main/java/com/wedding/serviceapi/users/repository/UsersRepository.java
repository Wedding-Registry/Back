package com.wedding.serviceapi.users.repository;

import com.wedding.serviceapi.users.domain.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public interface UsersRepository extends JpaRepository<Users, Long> {

    Optional<Users> findByNameAndEmail(String name, String email);

    Optional<Users> findByEmail(String email);

    Optional<Users> findByPassword(String password);

    Optional<Users> findByName(String name);

    List<Users> findByIdIn(List<Long> ids);

    default Map<Long, Users> findByIdInByMap(List<Long> ids) {
        return findByIdIn(ids).stream().collect(Collectors.toMap(Users::getId, v -> v));
    }
}
