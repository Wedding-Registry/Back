package com.wedding.serviceapi.goods.service;

import com.wedding.serviceapi.goods.domain.Commerce;
import com.wedding.serviceapi.goods.domain.Goods;
import com.wedding.serviceapi.goods.domain.UsersGoods;
import com.wedding.serviceapi.goods.dto.UsersGoodsPostResponseDto;
import com.wedding.serviceapi.goods.repository.GoodsRepository;
import com.wedding.serviceapi.goods.repository.UsersGoodsRepository;
import com.wedding.serviceapi.users.domain.LoginType;
import com.wedding.serviceapi.users.domain.Users;
import com.wedding.serviceapi.users.repository.UsersRepository;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import javax.persistence.EntityManager;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@Transactional
@Slf4j
class UsersGoodsServiceTest {

    @InjectMocks
    UsersGoodsService usersGoodsService;

    @Mock
    UsersGoodsRepository usersGoodsRepository;
    @Mock
    UsersRepository usersRepository;
    @Mock
    GoodsRepository goodsRepository;

    public String url;
    public Long userId;
    public Goods goods;
    public Users users;
    public UsersGoods usersGoods;

    @BeforeEach
    void setting() {
        url = "testUrl";
        userId = 1L;
        goods = new Goods("imgUrl", url, "goods1", 100000, Commerce.COUPANG);
        users = Users.builder().id(userId).build();
        usersGoods = new UsersGoods(users, goods);
    }

    @Test
    @DisplayName("상품 URL 등록")
    void postUsersGoods() {
        // given
        doReturn(users).when(usersRepository).getReferenceById(userId);
        doReturn(goods).when(goodsRepository).save(any(Goods.class));
        doReturn(usersGoods).when(usersGoodsRepository).save(any(UsersGoods.class));

        // when
        UsersGoodsPostResponseDto usersGoodsPostResponseDto = usersGoodsService.postUsersGoods(userId, url);

        // then
        assertThat(usersGoodsPostResponseDto.getUsersGoodsId()).isEqualTo(usersGoods.getId());
        assertThat(usersGoodsPostResponseDto.getUsersGoodsName()).isEqualTo("goods1");
        assertThat(usersGoodsPostResponseDto.getUsersGoodsPrice()).isEqualTo(100000);
        assertThat(usersGoodsPostResponseDto.getUsersGoodsImgUrl()).isEqualTo("imgUrl");

        verify(usersRepository, times(1)).getReferenceById(userId);
        verify(goodsRepository, times(1)).save(any(Goods.class));
        verify(usersGoodsRepository, times(1)).save(any(UsersGoods.class));
    }

    @Test
    @DisplayName("상품 이름 변경 성공")
    void changeUsersGoodsName() {
        // given
        assertThat(usersGoods.getUpdatedUsersGoodsName()).isEqualTo("goods1");
        String changedName = "testName";
        doReturn(Optional.of(usersGoods)).when(usersGoodsRepository).findById(usersGoods.getId());

        // when
        usersGoodsService.updateUsersGoodsName(usersGoods.getId(), changedName);

        // then
        assertThat(usersGoods.getUpdatedUsersGoodsName()).isEqualTo("testName");
    }

    @Test
    @DisplayName("상품 이름 변경 시 해당 상품이 없는 경우")
    void notFoundUsersGoods() {
        // given
        String changedName = "testName";
        Long usersGoodsId = 1L;
        doReturn(Optional.empty()).when(usersGoodsRepository).findById(anyLong());

        // when
        assertThrows(NoSuchElementException.class, () -> usersGoodsService.updateUsersGoodsName(usersGoodsId, changedName));
    }

}









































