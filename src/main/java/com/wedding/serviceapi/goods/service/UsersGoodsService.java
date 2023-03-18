package com.wedding.serviceapi.goods.service;

import com.wedding.serviceapi.goods.domain.Commerce;
import com.wedding.serviceapi.goods.domain.Goods;
import com.wedding.serviceapi.goods.domain.UsersGoods;
import com.wedding.serviceapi.goods.dto.UsersGoodsInfoDto;
import com.wedding.serviceapi.goods.dto.UsersGoodsPostResponseDto;
import com.wedding.serviceapi.goods.repository.GoodsRepository;
import com.wedding.serviceapi.goods.repository.UsersGoodsRepository;
import com.wedding.serviceapi.users.domain.Users;
import com.wedding.serviceapi.users.repository.UsersRepository;
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

    @Transactional
    public List<UsersGoodsInfoDto> findAllUsersGoods(Long userId) {
        List<UsersGoods> usersGoodsList = usersGoodsRepository.findByUsersId(userId);

        return usersGoodsList.stream().map(UsersGoodsInfoDto::new).collect(Collectors.toList());
    }

    public UsersGoodsPostResponseDto postUsersGoods(Long userId, String url) {
        // TODO: 2023/03/09 크롤링 서버로 url을 담아 요청을 보내서 상품 이름, 가격 등의 정보를 가져온다.
        Goods goods = new Goods("imgUrl", url, "goods1", 100000, Commerce.COUPANG);
        Users user = usersRepository.getReferenceById(userId);

        Goods savedGoods = goodsRepository.save(goods);

        UsersGoods usersGoods = new UsersGoods(user, savedGoods);
        UsersGoods savedUsersGoods = usersGoodsRepository.save(usersGoods);

        return new UsersGoodsPostResponseDto(savedUsersGoods.getId(),
                savedGoods.getGoodsImgUrl(),
                savedUsersGoods.getUpdatedUsersGoodsName(),
                savedUsersGoods.getUpdatedUsersGoodsPrice());
    }

    public void updateUsersGoodsName(Long userId, Long usersGoodsId, String usersGoodsName) {
        UsersGoods usersGoods = usersGoodsRepository.findByIdAndUsersId(userId, usersGoodsId).orElseThrow(() -> new NoSuchElementException("해당하는 상품이 없습니다."));
        usersGoods.changeUsersGoodsName(usersGoodsName);
    }

    public void updateUsersGoodsPrice(Long userId, Long usersGoodsId, Integer usersGoodsPrice) {
        UsersGoods usersGoods = usersGoodsRepository.findByIdAndUsersId(userId, usersGoodsId).orElseThrow(() -> new NoSuchElementException("해당하는 상품이 없습니다."));
        usersGoods.changeUsersGoodsPrice(usersGoodsPrice);
    }

    public void deleteUsersGoods(Long usersGoodsId) {
        UsersGoods usersGoods = usersGoodsRepository.findById(usersGoodsId).orElseThrow(() -> new NoSuchElementException("해당하는 상품이 없습니다."));
        usersGoodsRepository.delete(usersGoods);
    }
}
