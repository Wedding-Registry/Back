package com.wedding.serviceapi.goods.service;

import com.wedding.serviceapi.common.jwtutil.AuthJwtUtil;
import com.wedding.serviceapi.boards.domain.Boards;
import com.wedding.serviceapi.boards.repository.BoardsRepository;
import com.wedding.serviceapi.goods.domain.Commerce;
import com.wedding.serviceapi.goods.domain.Goods;
import com.wedding.serviceapi.goods.domain.UsersGoods;
import com.wedding.serviceapi.goods.dto.*;
import com.wedding.serviceapi.goods.repository.GoodsRepository;
import com.wedding.serviceapi.goods.repository.UsersGoodsRepository;
import com.wedding.serviceapi.users.domain.Role;
import com.wedding.serviceapi.users.domain.Users;
import com.wedding.serviceapi.users.repository.UsersRepository;
import com.wedding.serviceapi.util.crawling.RegisterUsersGoodsCrawler;
import com.wedding.serviceapi.util.crawling.GoodsRegisterResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Document;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class UsersGoodsService {

    private final UsersGoodsRepository usersGoodsRepository;
    private final UsersRepository usersRepository;
    private final GoodsRepository goodsRepository;
    private final RegisterUsersGoodsCrawler crawler;
    private final BoardsRepository boardsRepository;
    private final AuthJwtUtil jwtUtil;

    public MakeBoardResponseDto makeWeddingBoard(Long userId, String userName) {
        Users users = usersRepository.getReferenceById(userId);
        String uuidFirst = UUID.randomUUID().toString();
        String uuidSecond = UUID.randomUUID().toString();
        Boards boards = Boards.builder().users(users).uuidFirst(uuidFirst).uuidSecond(uuidSecond).build();

        // 이미 삭제하지 않은 board가 있는 경우 생성할 수 없다.
        boolean isBoardExisted = boardsRepository.findByUsersIdNotDeleted(userId).isPresent();
        if (isBoardExisted) {
            throw new IllegalArgumentException("이미 만들고 있는 청첩장이 존재합니다.");
        }
        Boards savedBoards = boardsRepository.save(boards);
        ArrayList<String> tokenList = jwtUtil.makeAccessTokenAndRefreshToken(userId, userName, savedBoards.getId(), Role.USER);
        users.setRefreshToken(tokenList.get(1));
        return new MakeBoardResponseDto(savedBoards, tokenList.get(0), tokenList.get(1));
    }

    public List<UsersGoodsInfoDto> findAllUsersGoods(Long userId, Long boardId) {
        List<UsersGoods> usersGoodsList = usersGoodsRepository.findAllByUsersIdAndBoardsIdNotWish(userId, boardId);

        return usersGoodsList.stream().map(UsersGoodsInfoDto::new).collect(Collectors.toList());
    }

    public List<UsersGoodsInfoDto> findAllUsersGoods(Long boardId) {
        List<UsersGoods> usersGoodsList = usersGoodsRepository.findAllByBoardsIdNotWish(boardId);

        return usersGoodsList.stream().map(UsersGoodsInfoDto::new).collect(Collectors.toList());
    }

    public UsersGoodsPostResponseDto postUsersGoods(Long userId, String url, Long boardId, boolean wishItem) {
        GoodsRegisterResponseDto goodsInfo = crawlingGoods(url);

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
        Boards board = boardsRepository.getReferenceById(boardId);

        UsersGoods usersGoods = new UsersGoods(user, goods, board, wishItem);
        UsersGoods savedUsersGoods;
        try {
            savedUsersGoods = usersGoodsRepository.save(usersGoods);
        } catch (DataIntegrityViolationException e) {
            throw new IllegalArgumentException("해당하는 게시판이 좋재하지 않습니다.", e);
        }

        return new UsersGoodsPostResponseDto(savedUsersGoods.getId(),
                goods.getGoodsImgUrl(),
                savedUsersGoods.getUpdatedUsersGoodsName(),
                savedUsersGoods.getUpdatedUsersGoodsPrice());
    }

    private GoodsRegisterResponseDto crawlingGoods(String url) {
        Document document = crawler.crawlWebPage(url);
        String productName = crawler.getProductName(document);
        Integer productCurrentPrice = crawler.getProductCurrentPrice(document);
        String productImgUrl = crawler.getProductImgUrl(document);
        GoodsRegisterResponseDto goodsInfo = new GoodsRegisterResponseDto(productName, productCurrentPrice, productImgUrl);
        return goodsInfo;
    }

    public UsersGoodsPostResponseDto updateUsersGoodsName(Long userId, Long usersGoodsId, String usersGoodsName) {
        UsersGoods usersGoods = usersGoodsRepository.findByIdAndUsersId(usersGoodsId, userId).orElseThrow(() -> new NoSuchElementException("해당하는 상품이 없습니다."));
        usersGoods.changeUsersGoodsName(usersGoodsName);
        return UsersGoodsPostResponseDto.from(usersGoods);
    }

    public UsersGoodsPostResponseDto updateUsersGoodsPrice(Long userId, Long usersGoodsId, Integer usersGoodsPrice) {
        UsersGoods usersGoods = usersGoodsRepository.findByIdAndUsersId(usersGoodsId, userId).orElseThrow(() -> new NoSuchElementException("해당하는 상품이 없습니다."));
        usersGoods.changeUsersGoodsPrice(usersGoodsPrice);
        return UsersGoodsPostResponseDto.from(usersGoods);
    }

    public void deleteUsersGoods(Long usersGoodsId) {
        UsersGoods usersGoods = usersGoodsRepository.findById(usersGoodsId).orElseThrow(() -> new NoSuchElementException("해당하는 상품이 없습니다."));
        usersGoodsRepository.delete(usersGoods);
    }


}
