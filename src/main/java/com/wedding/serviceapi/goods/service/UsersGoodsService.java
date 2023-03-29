package com.wedding.serviceapi.goods.service;

import com.wedding.serviceapi.goods.domain.Commerce;
import com.wedding.serviceapi.goods.domain.Goods;
import com.wedding.serviceapi.goods.domain.UsersGoods;
import com.wedding.serviceapi.goods.dto.UsersGoodsInfoDto;
import com.wedding.serviceapi.goods.dto.UsersGoodsNameDto;
import com.wedding.serviceapi.goods.dto.UsersGoodsPostResponseDto;
import com.wedding.serviceapi.goods.dto.UsersGoodsPriceDto;
import com.wedding.serviceapi.goods.repository.GoodsRepository;
import com.wedding.serviceapi.goods.repository.UsersGoodsRepository;
import com.wedding.serviceapi.users.domain.Users;
import com.wedding.serviceapi.users.repository.UsersRepository;
import com.wedding.serviceapi.util.webclient.GoodsRegisterResponseDto;
import com.wedding.serviceapi.util.webclient.WebClientUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class UsersGoodsService {

    private final UsersGoodsRepository usersGoodsRepository;
    private final UsersRepository usersRepository;
    private final GoodsRepository goodsRepository;
    private final WebClientUtil webClientUtil;

    @Transactional
    public List<UsersGoodsInfoDto> findAllUsersGoods(Long userId) {
        List<UsersGoods> usersGoodsList = usersGoodsRepository.findByUsersId(userId);

        return usersGoodsList.stream().map(UsersGoodsInfoDto::new).collect(Collectors.toList());
    }

    public UsersGoodsPostResponseDto postUsersGoods(Long userId, String url) {
        GoodsRegisterResponseDto goodsInfo = webClientUtil.getGoodsInfo(url);
        if (goodsInfo.getStatus() == 500) throw new IllegalArgumentException("잘못된 url 정보입니다.");
        log.info("goodsInfo = {}", goodsInfo);

        Goods goods;
        try {
            goods = goodsRepository.findByGoodsUrl(url).get();
            goods.updateGoodsInfo(goodsInfo);
        } catch (NoSuchElementException e) {
            goods = new Goods(goodsInfo.getGoodsImgUrl(), url, goodsInfo.getGoodsName(), goodsInfo.getGoodsPrice(), Commerce.NAVER);
            goods = goodsRepository.save(goods);
        }

        Users user = usersRepository.getReferenceById(userId);

        UsersGoods usersGoods = new UsersGoods(user, goods);
        UsersGoods savedUsersGoods = usersGoodsRepository.save(usersGoods);

        return new UsersGoodsPostResponseDto(savedUsersGoods.getId(),
                goods.getGoodsImgUrl(),
                savedUsersGoods.getUpdatedUsersGoodsName(),
                savedUsersGoods.getUpdatedUsersGoodsPrice());
    }

    public UsersGoodsNameDto updateUsersGoodsName(Long userId, Long usersGoodsId, String usersGoodsName) {
        UsersGoods usersGoods = usersGoodsRepository.findByIdAndUsersId(usersGoodsId, userId).orElseThrow(() -> new NoSuchElementException("해당하는 상품이 없습니다."));
        usersGoods.changeUsersGoodsName(usersGoodsName);
        return new UsersGoodsNameDto(usersGoods.getUpdatedUsersGoodsName());
    }

    public UsersGoodsPriceDto updateUsersGoodsPrice(Long userId, Long usersGoodsId, Integer usersGoodsPrice) {
        UsersGoods usersGoods = usersGoodsRepository.findByIdAndUsersId(usersGoodsId, userId).orElseThrow(() -> new NoSuchElementException("해당하는 상품이 없습니다."));
        usersGoods.changeUsersGoodsPrice(usersGoodsPrice);
        return new UsersGoodsPriceDto(usersGoods.getUpdatedUsersGoodsPrice());
    }

    public void deleteUsersGoods(Long usersGoodsId) {
        UsersGoods usersGoods = usersGoodsRepository.findById(usersGoodsId).orElseThrow(() -> new NoSuchElementException("해당하는 상품이 없습니다."));
        usersGoodsRepository.delete(usersGoods);
    }
}
