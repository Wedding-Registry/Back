package com.wedding.serviceapi.guests.repository;

import com.wedding.serviceapi.guests.domain.AccountTransfer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AccountTransferRepository extends JpaRepository<AccountTransfer, Long> {

    @Query("select a from AccountTransfer a where a.boards.id = :boardsId")
    List<AccountTransfer> findAllByBoardsId(@Param("boardsId") Long boardsId);

    @Query("select a from AccountTransfer a where a.id = :accountTransferId and a.boards.id = :boardsId")
    Optional<AccountTransfer> findByIdAndBoardsId(@Param("accountTransferId") Long accountTransferId, @Param("boardsId") Long boardsId);
}
