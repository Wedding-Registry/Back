package com.wedding.serviceapi.guests.repository;

import com.wedding.serviceapi.guests.domain.Guests;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface GuestsRepository extends JpaRepository<Guests, Long> {

    List<Guests> findAllByBoardsId(Long boardsId);
}
