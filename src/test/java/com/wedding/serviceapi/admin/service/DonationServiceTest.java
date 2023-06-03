package com.wedding.serviceapi.admin.service;

import com.wedding.serviceapi.admin.dto.donation.AccountTransferInfoDto;
import com.wedding.serviceapi.admin.dto.donation.DonatedUsersGoodsInfoDto;
import com.wedding.serviceapi.boards.domain.Boards;
import com.wedding.serviceapi.boards.repository.BoardsRepository;
import com.wedding.serviceapi.goods.domain.Commerce;
import com.wedding.serviceapi.goods.domain.Goods;
import com.wedding.serviceapi.goods.domain.UsersGoods;
import com.wedding.serviceapi.goods.repository.GoodsRepository;
import com.wedding.serviceapi.goods.repository.UsersGoodsRepository;
import com.wedding.serviceapi.guests.domain.AccountTransfer;
import com.wedding.serviceapi.guests.domain.GoodsDonation;
import com.wedding.serviceapi.guests.domain.Guests;
import com.wedding.serviceapi.guests.repository.AccountTransferRepository;
import com.wedding.serviceapi.guests.repository.GoodsDonationRepository;
import com.wedding.serviceapi.guests.repository.GuestsRepository;
import com.wedding.serviceapi.users.domain.LoginType;
import com.wedding.serviceapi.users.domain.Users;
import com.wedding.serviceapi.users.repository.UsersRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
@Slf4j
class DonationServiceTest {

    @Autowired
    DonationService donationService;
    @Autowired
    UsersRepository usersRepository;
    @Autowired
    BoardsRepository boardsRepository;
    @Autowired
    GoodsRepository goodsRepository;
    @Autowired
    UsersGoodsRepository usersGoodsRepository;
    @Autowired
    EntityManager em;
    @Autowired
    GuestsRepository guestsRepository;
    @Autowired
    GoodsDonationRepository goodsDonationRepository;
    @Autowired
    AccountTransferRepository accountTransferRepository;

    private Boards savedBoards;
    private Users savedUsers;

    @BeforeEach
    void setting() {
        String url = "testUrl";
        Goods goods1 = new Goods("imgUrl1", url, "goods1", 100000, Commerce.NAVER);
        Goods goods2 = new Goods("imgUrl2", url, "goods2", 200000, Commerce.NAVER);
        Users users = Users.builder().email("test").password("password").loginType(LoginType.KAKAO).build();
        Boards boards = Boards.builder().users(users).uuidFirst("first").uuidSecond("second").build();

        savedUsers = usersRepository.save(users);
        savedBoards = boardsRepository.save(boards);
        goodsRepository.save(goods1);
        goodsRepository.save(goods2);

        UsersGoods usersGoods1 = UsersGoods.builder().users(savedUsers).goods(goods1).boards(savedBoards).updatedUsersGoodsName(goods1.getGoodsName()).updatedUsersGoodsPrice(goods1.getGoodsPrice()).wishGoods(false).build();
        UsersGoods usersGoods2 = UsersGoods.builder().users(savedUsers).goods(goods2).boards(savedBoards).updatedUsersGoodsName(goods2.getGoodsName()).updatedUsersGoodsPrice(goods2.getGoodsPrice()).wishGoods(false).build();
        usersGoodsRepository.save(usersGoods1);
        usersGoodsRepository.save(usersGoods2);

        em.flush();
        em.clear();

        // 게스트 유저 추가
        Users guest1 = Users.builder().name("guest1").email("guest1").password("password1").loginType(LoginType.KAKAO).build();
        Users guest2 = Users.builder().name("guest2").email("guest2").password("password2").loginType(LoginType.KAKAO).build();
        Users guest3 = Users.builder().name("guest3").email("guest3").password("password3").loginType(LoginType.KAKAO).build();
        usersRepository.saveAllAndFlush(List.of(guest1, guest2, guest3));
        // 게스트 테이블 추가
        Guests invitedGuest1 = Guests.builder().users(guest1).boards(savedBoards).build();
        Guests invitedGuest2 = Guests.builder().users(guest2).boards(savedBoards).build();
        Guests invitedGuest3 = Guests.builder().users(guest3).boards(savedBoards).build();
        guestsRepository.saveAllAndFlush(List.of(invitedGuest1, invitedGuest2, invitedGuest3));
        // goodsDonation 테이블 추가
        GoodsDonation goodsDonation1 = GoodsDonation.builder().guests(invitedGuest1).usersGoods(usersGoods1).goodsDonationAmount(10000).build();
        GoodsDonation goodsDonation2 = GoodsDonation.builder().guests(invitedGuest2).usersGoods(usersGoods1).goodsDonationAmount(20000).build();
        GoodsDonation goodsDonation3 = GoodsDonation.builder().guests(invitedGuest2).usersGoods(usersGoods2).goodsDonationAmount(30000).build();
        GoodsDonation goodsDonation4 = GoodsDonation.builder().guests(invitedGuest3).usersGoods(usersGoods2).goodsDonationAmount(40000).build();
        goodsDonationRepository.saveAllAndFlush(List.of(goodsDonation1, goodsDonation2, goodsDonation3, goodsDonation4));
    }

    @Test
    @DisplayName("상품 후원 조회 서비스 정상 동작 테스트")
    void 상품_후원_조회_테스트() {
        // when
        List<DonatedUsersGoodsInfoDto> allUsersGoodsInfo = donationService.findAllUsersGoodsInfo(savedUsers.getId(), savedBoards.getId());

        // then
        assertThat(allUsersGoodsInfo.size()).isEqualTo(2);
        assertThat(allUsersGoodsInfo.get(0).getDonationList().size()).isEqualTo(2);
        assertThat(allUsersGoodsInfo.get(0).getDonationList().get(0).getName()).isEqualTo("guest1");
        assertThat(allUsersGoodsInfo.get(0).getDonationList().get(1).getName()).isEqualTo("guest2");
        assertThat(allUsersGoodsInfo.get(1).getDonationList().size()).isEqualTo(2);
        assertThat(allUsersGoodsInfo.get(1).getDonationList().get(0).getName()).isEqualTo("guest2");
        assertThat(allUsersGoodsInfo.get(1).getDonationList().get(1).getName()).isEqualTo("guest3");
    }

    @Test
    @DisplayName("계좌 이체 내역 조회 정상 동작 테스트")
    void 계좌_이체_조회_테스트() {
        // given
        Boards boards = boardsRepository.findById(savedBoards.getId()).get();
        AccountTransfer accountTransfer1 = AccountTransfer.builder().boards(boards).transferMemo("test1 memo").build();
        AccountTransfer accountTransfer2 = AccountTransfer.builder().boards(boards).transferMemo("test2 memo").build();
        AccountTransfer accountTransfer3 = AccountTransfer.builder().boards(boards).transferMemo("test3 memo").build();
        accountTransferRepository.saveAllAndFlush(List.of(accountTransfer1, accountTransfer2, accountTransfer3));

        // when
        List<AccountTransferInfoDto> test = donationService.findAllAccountTransferInfo(savedBoards.getId());
        // then
        assertThat(test.size()).isEqualTo(3);
    }

    @Test
    @DisplayName("해당하는 결혼 게시판이 없어서 실패")
    void 계좌이체메모_실패() {
        // given
        String transferMemo = "testMemo";
        Long boardsId = 100L;
        // then
        assertThatThrownBy(() -> donationService.postAccountTransferMemo(boardsId, transferMemo))
                .isInstanceOf(NoSuchElementException.class);
    }

    @Test
    @DisplayName("해당하는 결혼 게시판에 계좌 이체 정보 추가 성공")
    void 계좌이체메모_성공() {
        // given
        String transferMemo = "testMemo";
        // when
        AccountTransferInfoDto accountTransferInfoDto = donationService.postAccountTransferMemo(savedBoards.getId(), transferMemo);
        // then
        assertThat(accountTransferInfoDto.getTransferMemo()).isEqualTo("testMemo");
    }

    @Test
    @DisplayName("해당하는 결혼 게시판이 없어서 실패")
    void 계좌이체메모수정_실패() {
        // given
        String transferMemo = "testMemo";
        Long boardsId = 1L;
        Long accountTransferId = 2L;
        // then
        assertThatThrownBy(() -> donationService.putAccountTransferMemo(boardsId, accountTransferId, transferMemo))
                .isInstanceOf(NoSuchElementException.class);
    }

    @Test
    @DisplayName("해당하는 결혼 게시판 계좌 메모 수정 성공")
    void 계좌이체메모수정_성공() {
        // given
        String transferMemo = "testMemo";
        Long boardsId = 1L;
        AccountTransferInfoDto accountTransferInfoDto = donationService.postAccountTransferMemo(boardsId, transferMemo);
        String newTransferMemo = "new test memo";
        // when
        AccountTransferInfoDto newAccountTransferData = donationService.putAccountTransferMemo(boardsId, accountTransferInfoDto.getAccountTransferId(), newTransferMemo);
        // then
        assertThat(newAccountTransferData.getTransferMemo()).isEqualTo("new test memo");
    }

    @Test
    @DisplayName("계좌이체메모 삭제 성공")
    void 계좌이체_삭제_테스트() {
        // given
        String transferMemo = "testMemo";
        AccountTransferInfoDto accountTransferInfoDto = donationService.postAccountTransferMemo(savedBoards.getId(), transferMemo);
        // when
        donationService.deleteAccountTransferMemo(savedBoards.getId(), accountTransferInfoDto.getAccountTransferId());
        // then
        List<AccountTransferInfoDto> testData = donationService.findAllAccountTransferInfo(savedBoards.getId());
        assertThat(testData.size()).isEqualTo(0);
    }
}