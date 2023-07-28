package com.wedding.serviceapi.admin.controller;


import com.wedding.serviceapi.admin.dto.memo.PadContentsDto;
import com.wedding.serviceapi.admin.dto.memo.WishItemPagingDto;
import com.wedding.serviceapi.admin.service.MemoService;
import com.wedding.serviceapi.auth.vo.LoginUser;
import com.wedding.serviceapi.auth.vo.LoginUserVo;
import com.wedding.serviceapi.common.vo.ResponseVo;
import com.wedding.serviceapi.goods.dto.UsersGoodsPostResponseDto;
import com.wedding.serviceapi.goods.service.UsersGoodsService;
import com.wedding.serviceapi.goods.vo.PostUsersGoodsRequestVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/memo")
@Slf4j
@RequiredArgsConstructor
public class MemoController {

    private final MemoService memoService;
    private final UsersGoodsService usersGoodsService;

    @GetMapping("/item/wish")
    public ResponseVo<WishItemPagingDto> getWishItemList(@LoginUser LoginUserVo loginUserVo,
                                                         @RequestParam("lastId") Long lastId,
                                                         Pageable pageable) {
        log.info("lastId = {}, size = {}, sort = {}", lastId, pageable.getPageSize(), pageable.getSort());
        WishItemPagingDto data = memoService.getAllItemWish(loginUserVo.getUserId(), loginUserVo.getBoardsId(), lastId, pageable);

        return ResponseVo.ok(data);
    }

    @PostMapping("/item/wish")
    public ResponseVo<UsersGoodsPostResponseDto> postWishItem(@LoginUser LoginUserVo loginUserVo, @Validated @RequestBody PostUsersGoodsRequestVo requestVo) {
        UsersGoodsPostResponseDto data = usersGoodsService.postUsersGoods(loginUserVo.getUserId(), requestVo.getUrl(), loginUserVo.getBoardsId(), true);

        return ResponseVo.created(data);
    }

    @GetMapping("/pad")
    public ResponseVo<PadContentsDto> getMemoPad(@LoginUser LoginUserVo loginUserVo) {
        PadContentsDto data = memoService.getMemoContents(loginUserVo.getUserId(), loginUserVo.getBoardsId());

        return ResponseVo.ok(data);
    }

    @PostMapping("/pad")
    public ResponseVo<PadContentsDto> postMemoPad(@LoginUser LoginUserVo loginUserVo, @Validated @RequestBody PadContentsDto padContentsDto) {
        PadContentsDto data = memoService.postMemoContents(loginUserVo.getUserId(), loginUserVo.getBoardsId(), padContentsDto.getContents());

        return ResponseVo.created(data);
    }

    @DeleteMapping("/pad")
    public ResponseVo<Void> deleteMemoPad(@LoginUser LoginUserVo loginUserVo) {
        memoService.deleteMemoContents(loginUserVo.getUserId(), loginUserVo.getBoardsId());

        return ResponseVo.accepted();
    }
}
