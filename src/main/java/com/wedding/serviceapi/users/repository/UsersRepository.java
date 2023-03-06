package com.wedding.serviceapi.users.repository;

import com.wedding.serviceapi.users.domain.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsersRepository extends JpaRepository<Users, Long> {
}
