package com.wedding.serviceapi.admin.service;

import com.wedding.serviceapi.admin.domain.PagingManager;
import com.wedding.serviceapi.admin.dto.memo.PadContentsDto;
import com.wedding.serviceapi.admin.dto.memo.WishItemPagingDto;
import com.wedding.serviceapi.boards.domain.Boards;
import com.wedding.serviceapi.boards.repository.BoardsRepository;
import com.wedding.serviceapi.goods.domain.UsersGoods;
import com.wedding.serviceapi.goods.repository.UsersGoodsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class MemoService {

    private final UsersGoodsRepository usersGoodsRepository;
    private final BoardsRepository boardsRepository;

    public WishItemPagingDto getAllItemWish(Long usersId, Long boardsId, Long lastId, Pageable pageable) {
        Slice<UsersGoods> usersGoodsSlice = usersGoodsRepository.findByUsersIdAndBoardsIdAndWishGoodsAndIdLessThan(usersId, boardsId, true, lastId, pageable);
        PagingManager pagingManager = new PagingManager(usersGoodsSlice);
        return pagingManager.makeWishItemPagingDto();
    }

    public PadContentsDto getMemoContents(Long usersId, Long boardsId) {
        Boards boards = boardsRepository.findByIdAndUsersIdNotDeleted(boardsId, usersId).orElseThrow(() -> new NoSuchElementException("해당하는 웨딩 게시판이 없습니다."));
        return PadContentsDto.from(boards);
    }

    @Transactional
    public PadContentsDto postMemoContents(Long usersId, Long boardsId, String memo) {
        Boards boards = boardsRepository.findByIdAndUsersIdNotDeleted(boardsId, usersId).orElseThrow(() -> new NoSuchElementException("해당하는 웨딩 게시판이 없습니다."));
        boards.updateMemo(memo);

        return PadContentsDto.from(boards);
    }

    @Transactional
    public void deleteMemoContents(Long usersId, Long boardsId) {
        Boards boards = boardsRepository.findByIdAndUsersIdNotDeleted(boardsId, usersId).orElseThrow(() -> new NoSuchElementException("해당하는 웨딩 게시판이 없습니다."));
        boards.deleteMemo();
    }
}
