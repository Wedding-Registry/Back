package com.wedding.serviceapi.boards.repository;

import com.wedding.serviceapi.boards.domain.Boards;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BoardsRepository extends JpaRepository<Boards, Long> {

    Optional<Boards> findByIdOrParentsId(Long usersId);
}
