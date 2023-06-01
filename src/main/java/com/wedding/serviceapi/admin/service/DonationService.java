package com.wedding.serviceapi.admin.service;

import com.wedding.serviceapi.admin.dto.donation.AccountTransferInfoDto;
import com.wedding.serviceapi.admin.dto.donation.DonatedUsersGoodsInfoDto;
import com.wedding.serviceapi.boards.domain.Boards;
import com.wedding.serviceapi.boards.repository.BoardsRepository;
import com.wedding.serviceapi.goods.domain.UsersGoods;
import com.wedding.serviceapi.goods.repository.UsersGoodsRepository;
import com.wedding.serviceapi.guests.domain.AccountTransfer;
import com.wedding.serviceapi.guests.repository.AccountTransferRepository;
import com.wedding.serviceapi.users.domain.Users;
import com.wedding.serviceapi.users.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class DonationService {

    private final UsersGoodsRepository usersGoodsRepository;
    private final UsersRepository usersRepository;
    private final AccountTransferRepository accountTransferRepository;
    private final BoardsRepository boardsRepository;

    @Transactional(readOnly = true)
    public List<DonatedUsersGoodsInfoDto> findAllUsersGoodsInfo(Long usersId, Long boardsId) {
        List<UsersGoods> usersGoodsList = usersGoodsRepository.findAllDistinctByUsersIdAndBoardsIdNotWishWithUrlAndDonationId(usersId, boardsId);
        List<Long> guestsIdList = usersGoodsList.stream()
                .flatMap(el -> el.getDonationList().stream())
                .map(goodsDonation -> goodsDonation.getGuests().getUsers().getId())
                .distinct().collect(Collectors.toList());

        Map<Long, Users> usersMap = usersRepository.findByIdInByMap(guestsIdList);

        return usersGoodsList.stream()
                .map(usersGoods -> DonatedUsersGoodsInfoDto.from(usersGoods, usersMap))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<AccountTransferInfoDto> findAllAccountTransferInfo(Long boardsId) {
        List<AccountTransfer> accountTransferList = accountTransferRepository.findAllByBoardsId(boardsId);
        return accountTransferList.stream().map(AccountTransferInfoDto::from).collect(Collectors.toList());
    }

    public AccountTransferInfoDto postAccountTransferMemo(Long boardsId, String transferMemo) {
        Boards boards = boardsRepository.findById(boardsId).orElseThrow(() -> new NoSuchElementException("잘못된 웨딩 게시판 정보입니다."));
        AccountTransfer accountTransfer = AccountTransfer.builder().boards(boards).transferMemo(transferMemo).build();
        return AccountTransferInfoDto.from(accountTransferRepository.save(accountTransfer));
    }

    public AccountTransferInfoDto putAccountTransferMemo(Long boardsId, Long accountTransferId, String transferMemo) {
        AccountTransfer accountTransfer = accountTransferRepository.findByIdAndBoardsId(accountTransferId, boardsId)
                .orElseThrow(() -> new NoSuchElementException("잘못된 웨딩 게시판 정보입니다."));
        accountTransfer.changeMemo(transferMemo);
        return AccountTransferInfoDto.from(accountTransfer);
    }

    public void deleteAccountTransferMemo(Long boardsId, Long accountTransferId) {
        AccountTransfer accountTransfer = accountTransferRepository.findByIdAndBoardsId(accountTransferId, boardsId)
                .orElseThrow(() -> new NoSuchElementException("해당하는 웨딩 게시판이 없습니다."));
        accountTransferRepository.delete(accountTransfer);
    }
}































