package com.wedding.serviceapi.goods.repository;

import com.wedding.serviceapi.goods.domain.UsersGoods;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UsersGoodsRepository extends JpaRepository<UsersGoods, Long> {

    Optional<UsersGoods> findByIdAndUsersId(Long id, Long usersId);

    @Query("select u from UsersGoods u join fetch u.goods where u.users.id = :userId")
    List<UsersGoods> findByUsersId(Long usersId);

}
