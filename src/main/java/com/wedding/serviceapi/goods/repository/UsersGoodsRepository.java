package com.wedding.serviceapi.goods.repository;

import com.wedding.serviceapi.goods.domain.UsersGoods;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsersGoodsRepository extends JpaRepository<UsersGoods, Long> {
}
