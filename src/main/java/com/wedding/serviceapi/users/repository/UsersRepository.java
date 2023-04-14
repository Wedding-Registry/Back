package com.wedding.serviceapi.users.repository;

import com.wedding.serviceapi.users.domain.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Map;
import java.util.Optional;

public interface UsersRepository extends JpaRepository<Users, Long> {

    Optional<Users> findByNameAndEmail(String name, String email);

    Optional<Users> findByEmail(String email);
}
