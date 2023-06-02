package com.wedding.serviceapi.guests.repository;

import com.wedding.serviceapi.guests.domain.GoodsDonation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface GoodsDonationRepository extends JpaRepository<GoodsDonation, Long> {

    @Query("select gd from GoodsDonation gd join fetch gd.guests g join fetch g.users u join fetch gd.usersGoods" +
            " where g.boards.id = :boardsId order by gd.updatedAt desc")
    List<GoodsDonation> findAllByBoardsIdWithUser(@Param("boardsId") Long boardsId);
}
