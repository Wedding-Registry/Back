package com.wedding.serviceapi.goods.repository;

import com.wedding.serviceapi.goods.domain.Goods;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GoodsRepository extends JpaRepository<Goods, Long> {
}
