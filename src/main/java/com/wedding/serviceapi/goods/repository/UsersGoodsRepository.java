package com.wedding.serviceapi.goods.repository;

import com.wedding.serviceapi.goods.domain.UsersGoods;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UsersGoodsRepository extends JpaRepository<UsersGoods, Long> {

    Optional<UsersGoods> findByIdAndUsersId(Long id, Long usersId);

    @Query("select u from UsersGoods u join fetch u.goods where u.users.id = :userId and u.boards.id = :boardId and u.wishGoods = false")
    List<UsersGoods> findAllByUsersIdAndBoardsIdNotWish(@Param("userId") Long userId, @Param("boardId") Long boardId);

}
