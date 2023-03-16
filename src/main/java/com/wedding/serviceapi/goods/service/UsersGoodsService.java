package com.wedding.serviceapi.goods.service;

import com.wedding.serviceapi.goods.domain.Commerce;
import com.wedding.serviceapi.goods.domain.Goods;
import com.wedding.serviceapi.goods.domain.UsersGoods;
import com.wedding.serviceapi.goods.dto.UsersGoodsPostResponseDto;
import com.wedding.serviceapi.goods.repository.GoodsRepository;
import com.wedding.serviceapi.goods.repository.UsersGoodsRepository;
import com.wedding.serviceapi.users.domain.Users;
import com.wedding.serviceapi.users.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UsersGoodsService {

    private final UsersGoodsRepository usersGoodsRepository;
    private final UsersRepository usersRepository;
    private final GoodsRepository goodsRepository;

    @Transactional
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

    @Transactional
    public void updateUsersGoodsName(Long usersGoodsId, String usersGoodsName) {
        UsersGoods usersGoods = usersGoodsRepository.findById(usersGoodsId).orElseThrow(() -> new NoSuchElementException("해당하는 상품이 없습니다."));
        usersGoods.changeUsersGoodsName(usersGoodsName);
    }

}
