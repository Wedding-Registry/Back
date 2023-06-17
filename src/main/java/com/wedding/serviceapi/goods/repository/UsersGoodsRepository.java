package com.wedding.serviceapi.goods.repository;

import com.wedding.serviceapi.goods.domain.UsersGoods;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UsersGoodsRepository extends JpaRepository<UsersGoods, Long> {

    Optional<UsersGoods> findByIdAndUsersId(Long id, Long usersId);

    // TODO: 2023/06/17 queryDsl 이용해서 동적쿼리로 생성 가능
    @Query("select u from UsersGoods u join fetch u.goods where u.users.id = :userId and u.boards.id = :boardId and u.wishGoods = false")
    List<UsersGoods> findAllByUsersIdAndBoardsIdNotWish(@Param("userId") Long userId, @Param("boardId") Long boardId);

    @Query("select u from UsersGoods u join fetch u.goods where u.boards.id = :boardId and u.wishGoods = false")
    List<UsersGoods> findAllByBoardsIdNotWish(@Param("boardId") Long boardId);

    @Query("select distinct u from UsersGoods u join fetch u.goods join fetch u.donationList where u.users.id = :userId and u.boards.id = :boardId and u.wishGoods = false")
    List<UsersGoods> findAllDistinctByUsersIdAndBoardsIdNotWishWithUrlAndDonationId(@Param("userId") Long userId, @Param("boardId") Long boardId);

    @EntityGraph(attributePaths = {"goods"})
    Slice<UsersGoods> findByUsersIdAndBoardsIdAndWishGoodsAndIdLessThan(Long usersId, Long boardsId, Boolean wishGoods, Long usersGoodsId, Pageable pageable);
}
