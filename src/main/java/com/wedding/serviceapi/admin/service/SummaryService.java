package com.wedding.serviceapi.admin.service;

import com.wedding.serviceapi.admin.domain.CalculateAttendance;
import com.wedding.serviceapi.admin.dto.summary.AttendanceSummaryDto;
import com.wedding.serviceapi.admin.dto.summary.DonationSummaryDto;
import com.wedding.serviceapi.goods.domain.UsersGoods;
import com.wedding.serviceapi.goods.repository.UsersGoodsRepository;
import com.wedding.serviceapi.guests.domain.AttendanceType;
import com.wedding.serviceapi.guests.domain.Guests;
import com.wedding.serviceapi.guests.repository.GuestsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class SummaryService {

    private final GuestsRepository guestsRepository;
    private final UsersGoodsRepository usersGoodsRepository;
    private final CalculateAttendance calculateAttendance;


    public AttendanceSummaryDto getAttendanceSummary(Long boardsId) {
        List<Guests> guestsList = guestsRepository.findAllByBoardsId(boardsId);

        return calculateAttendance.makeAttendanceSummary(guestsList);
    }

    public List<DonationSummaryDto> getDonationSummary(Long usersId, Long boardsId) {
        List<UsersGoods> usersGoodsList = usersGoodsRepository.findAllByUsersIdAndBoardsIdNotWish(usersId, boardsId);
        return usersGoodsList.stream().map(DonationSummaryDto::of)
                .sorted()
                .collect(Collectors.toList()).subList(0, 3);
    }
}
