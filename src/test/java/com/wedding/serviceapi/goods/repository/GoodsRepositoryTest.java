package com.wedding.serviceapi.goods.repository;

import com.wedding.serviceapi.goods.domain.Commerce;
import com.wedding.serviceapi.goods.domain.Goods;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import javax.persistence.EntityManager;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Slf4j
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class GoodsRepositoryTest {

    @Autowired
    GoodsRepository goodsRepository;
    @Autowired
    EntityManager em;

    @Test
    @DisplayName("상품 url로 찾기")
    void findByGoodsUrl() {
        // given
        Goods goods = new Goods("goodsname", "test2", "test", 2000, Commerce.NAVER);
        goodsRepository.save(goods);
        em.flush();
        em.clear();

        // when
        Optional<Goods> test = goodsRepository.findByGoodsUrl("test2");
        Goods foundedGoods = test.get();

        // then
        assertThat(foundedGoods.getGoodsName()).isEqualTo("test");
        assertThat(foundedGoods.getGoodsPrice()).isEqualTo(2000);
    }
}