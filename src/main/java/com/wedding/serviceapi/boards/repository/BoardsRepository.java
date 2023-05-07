package com.wedding.serviceapi.boards.repository;

import com.wedding.serviceapi.boards.domain.Boards;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface BoardsRepository extends JpaRepository<Boards, Long> {
    @Query("select b from Boards b where b.id = :boardsId and b.users.id = :usersId and b.deletedAt = false")
    Optional<Boards> findByIdAndUsersIdNotDeleted(@Param("boardsId") Long boardsId, @Param("usersId") Long usersId);

    @Query("select b from Boards b where b.users.id = :usersId and b.deletedAt = false")
    Optional<Boards> findByUsersIdNotDeleted(@Param("usersId") Long usersId);
}
