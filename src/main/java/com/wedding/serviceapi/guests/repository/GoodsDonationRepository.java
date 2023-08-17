package com.wedding.serviceapi.guests.repository;

import com.wedding.serviceapi.guests.domain.GoodsDonation;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface GoodsDonationRepository extends JpaRepository<GoodsDonation, Long> {
    // TODO: 2023/08/17 쿼리 리팩토링 필요

    @Query("select gd from GoodsDonation gd join fetch gd.guests g join fetch g.users u join fetch gd.usersGoods" +
            " where g.boards.id = :boardsId order by gd.updatedAt desc")
    List<GoodsDonation> findAllByBoardsIdWithUser(@Param("boardsId") Long boardsId);

    @Query("select gd from GoodsDonation gd join fetch gd.guests g join fetch g.users u join fetch gd.usersGoods" +
            " where g.boards.id = :boardsId order by gd.updatedAt desc")
    List<GoodsDonation> findLimit5ByBoardsIdWithUser(@Param("boardsId") Long boardsId, Pageable pageable);

    Optional<GoodsDonation> findByGuestsIdAndUsersGoodsId(Long guestsId, Long usersGoodsId);
}
