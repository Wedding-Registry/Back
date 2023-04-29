package com.wedding.serviceapi.goods.service;

import com.wedding.serviceapi.exception.NegativePriceException;
import com.wedding.serviceapi.goods.domain.Commerce;
import com.wedding.serviceapi.goods.domain.Goods;
import com.wedding.serviceapi.goods.domain.UsersGoods;
import com.wedding.serviceapi.goods.dto.UsersGoodsInfoDto;
import com.wedding.serviceapi.goods.dto.UsersGoodsPostResponseDto;
import com.wedding.serviceapi.goods.repository.GoodsRepository;
import com.wedding.serviceapi.goods.repository.UsersGoodsRepository;
import com.wedding.serviceapi.users.domain.LoginType;
import com.wedding.serviceapi.users.domain.Users;
import com.wedding.serviceapi.users.repository.UsersRepository;
import com.wedding.serviceapi.util.crawling.RegisterUsersGoodsCrawler;
import com.wedding.serviceapi.util.webclient.GoodsRegisterResponseDto;
import com.wedding.serviceapi.util.webclient.WebClientUtil;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.assertj.core.internal.Integers;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StreamUtils;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import javax.persistence.EntityManager;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
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
    @Mock
    RegisterUsersGoodsCrawler crawler;

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
    @DisplayName("등록한 상품 리스트 조회")
    void findAllUsersGoods() {
        // given
        Goods goods1 = new Goods("test1", "test1", "goods1", 10000, Commerce.COUPANG);
        Goods goods2 = new Goods("test2", "test2", "goods2", 20000, Commerce.COUPANG);
        Goods goods3 = new Goods("test3", "test3", "goods3", 30000, Commerce.COUPANG);
        UsersGoods usersGoods1 = new UsersGoods(users, goods1);
        UsersGoods usersGoods2 = new UsersGoods(users, goods2);
        UsersGoods usersGoods3 = new UsersGoods(users, goods3);
        List<UsersGoods> data = new ArrayList<>();
        data.add(usersGoods1);
        data.add(usersGoods2);
        data.add(usersGoods3);

        doReturn(data).when(usersGoodsRepository).findByUsersId(anyLong());
        
        // when
        List<UsersGoodsInfoDto> result = usersGoodsService.findAllUsersGoods(anyLong());

        // then;
        result.forEach(usersGoodsInfoDto -> assertThat(usersGoodsInfoDto.getUsersGoodsPercent()).isEqualTo(0));
        assertThat(result.size()).isEqualTo(3);
    }

    @Test
    @DisplayName("상품 URL 등록 - 기존 상품이 있는 경우")
    void postUsersGoodsExistedGoods() {
        // given
        GoodsRegisterResponseDto goodsRegisterResponseDto = new GoodsRegisterResponseDto("webclient", 250000, "webClient img");
        Document document = new Document(url);
        doReturn(document).when(crawler).crawlWebPage(url);
        doReturn("webclient").when(crawler).getProductName(document);
        doReturn(250000).when(crawler).getProductCurrentPrice(document);
        doReturn("webClient img").when(crawler).getProductImgUrl(document);
        doReturn(Optional.of(goods)).when(goodsRepository).findByGoodsUrl(anyString());
        doReturn(users).when(usersRepository).getReferenceById(userId);

        goods.updateGoodsInfo(goodsRegisterResponseDto);
        UsersGoods usersgoods = new UsersGoods(users, goods);

        doReturn(usersgoods).when(usersGoodsRepository).save(any(UsersGoods.class));

        // when
        UsersGoodsPostResponseDto usersGoodsPostResponseDto = usersGoodsService.postUsersGoods(userId, url);

        // then
        assertThat(usersGoodsPostResponseDto.getUsersGoodsId()).isEqualTo(usersGoods.getId());
        assertThat(usersGoodsPostResponseDto.getUsersGoodsName()).isEqualTo("webclient");
        assertThat(usersGoodsPostResponseDto.getUsersGoodsPrice()).isEqualTo(250000);
        assertThat(usersGoodsPostResponseDto.getUsersGoodsImgUrl()).isEqualTo("webClient img");

    }

    @Test
    @DisplayName("상품 URL 등록 - 기존 상품이 없는 경우")
    void postUsersGoodsNotExistedGoods() {
        // given
        GoodsRegisterResponseDto data = new GoodsRegisterResponseDto("test", 1000, "test");
        Goods newGoods = new Goods(data.getGoodsImgUrl(), "test", data.getGoodsName(), data.getGoodsPrice(), Commerce.NAVER);

        UsersGoods usersGoods = new UsersGoods(users, newGoods);

        Document document = new Document(url);
        doReturn(document).when(crawler).crawlWebPage(url);
        doReturn("test").when(crawler).getProductName(document);
        doReturn(1000).when(crawler).getProductCurrentPrice(document);
        doReturn("test").when(crawler).getProductImgUrl(document);

        doReturn(users).when(usersRepository).getReferenceById(userId);
        doReturn(newGoods).when(goodsRepository).save(any(Goods.class));
        doReturn(usersGoods).when(usersGoodsRepository).save(any(UsersGoods.class));
        doThrow(NoSuchElementException.class).when(goodsRepository).findByGoodsUrl(anyString());
        // when
        UsersGoodsPostResponseDto usersGoodsPostResponseDto = usersGoodsService.postUsersGoods(userId, url);

        // then
        assertThat(usersGoodsPostResponseDto.getUsersGoodsId()).isEqualTo(this.usersGoods.getId());
        assertThat(usersGoodsPostResponseDto.getUsersGoodsName()).isEqualTo("test");
        assertThat(usersGoodsPostResponseDto.getUsersGoodsPrice()).isEqualTo(1000);
        assertThat(usersGoodsPostResponseDto.getUsersGoodsImgUrl()).isEqualTo("test");

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
        doReturn(Optional.of(usersGoods)).when(usersGoodsRepository).findByIdAndUsersId(usersGoods.getId(), users.getId());

        // when
        usersGoodsService.updateUsersGoodsName(users.getId(), usersGoods.getId(), changedName);

        // then
        assertThat(usersGoods.getUpdatedUsersGoodsName()).isEqualTo("testName");
    }

    @Test
    @DisplayName("상품 이름 변경 시 해당 상품이 없는 경우")
    void notFoundUsersGoods() {
        // given
        String changedName = "testName";
        Long usersGoodsId = 1L;
        doReturn(Optional.empty()).when(usersGoodsRepository).findByIdAndUsersId(anyLong(), anyLong());

        // when
        assertThrows(NoSuchElementException.class, () -> usersGoodsService.updateUsersGoodsName(users.getId(), usersGoodsId, changedName));
    }

    @Test
    @DisplayName("상품 후원가 수정")
    void changeUsersGoodsPrice() {
        // given
        Integer newPrice = 1000;
        doReturn(Optional.of(usersGoods)).when(usersGoodsRepository).findByIdAndUsersId(usersGoods.getId(), users.getId());

        // when
        usersGoodsService.updateUsersGoodsPrice(users.getId(), usersGoods.getId(), newPrice);

        // then
        assertThat(usersGoods.getUpdatedUsersGoodsPrice()).isEqualTo(1000);
    }

    @ParameterizedTest
    @DisplayName("상품 후원가 변경시 가격에 0 미만 값이 들어온 경우")
    @ValueSource(ints = {-100, -1})
    void negativeUsersGoodsPrice(int price) {
        // given
        doReturn(Optional.of(usersGoods)).when(usersGoodsRepository).findByIdAndUsersId(usersGoods.getId(), users.getId());
        // when
        assertThrows(NegativePriceException.class, () -> usersGoodsService.updateUsersGoodsPrice(users.getId(), usersGoods.getId(), price));
    }

    @Test
    @DisplayName("상품 후원가 변경시 가격에 null 값이 들어온 경우")
    void nullUsersGoodsPrice() {
        // given
        Integer price = null;
        doReturn(Optional.of(usersGoods)).when(usersGoodsRepository).findByIdAndUsersId(usersGoods.getId(), users.getId());
        // when
        assertThrows(NegativePriceException.class, () -> usersGoodsService.updateUsersGoodsPrice(users.getId(), usersGoods.getId(), price));
    }
}









































