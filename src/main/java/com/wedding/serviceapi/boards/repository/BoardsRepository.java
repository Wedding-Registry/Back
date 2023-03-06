package com.wedding.serviceapi.boards.repository;

import com.wedding.serviceapi.boards.domain.Boards;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardsRepository extends JpaRepository<Boards, Long> {
}
