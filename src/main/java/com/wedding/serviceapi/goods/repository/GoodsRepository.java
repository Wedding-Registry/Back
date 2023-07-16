package com.wedding.serviceapi.goods.repository;

import com.wedding.serviceapi.goods.domain.Goods;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.swing.text.html.Option;
import java.util.Optional;

public interface GoodsRepository extends JpaRepository<Goods, Long> {

    Optional<Goods> findByGoodsUrl(String url);
}
