package com.wedding.serviceapi.guests.repository;

import com.wedding.serviceapi.guests.domain.Guests;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface GuestsRepository extends JpaRepository<Guests, Long> {

    List<Guests> findAllByBoardsId(Long boardsId);

    @Query("select g from Guests g join fetch g.users where g.boards.id = :boardsId")
    List<Guests> findAllByBoardsIdWithUsers(@Param("boardsId") Long boardsId);

    @Query("select g from Guests g where g.users.id = :usersId and g.boards.id = :boardsId")
    Optional<Guests> findByUsersIdAndBoardsId(@Param("usersId") Long usersId, @Param("boardsId") Long boardsId);

    @EntityGraph(attributePaths = {"users"})
    List<Guests> findAllByBoardsIdOrderByUpdatedAtDesc(Long boardsId);
}
