package com.wedding.serviceapi.admin.service;

import com.wedding.serviceapi.admin.dto.alarm.AlarmListResponseDto;
import com.wedding.serviceapi.admin.dto.alarm.DonationUserInfoDto;
import com.wedding.serviceapi.admin.dto.attendance.AttendanceUserInfoDto;
import com.wedding.serviceapi.guests.domain.GoodsDonation;
import com.wedding.serviceapi.guests.domain.Guests;
import com.wedding.serviceapi.guests.repository.GoodsDonationRepository;
import com.wedding.serviceapi.guests.repository.GuestsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@Slf4j
@RequiredArgsConstructor
public class AlarmService {

    private final GuestsRepository guestsRepository;
    private final GoodsDonationRepository goodsDonationRepository;

    public AlarmListResponseDto getAllAlarmList(Long boardsId) {
        List<Guests> guestList = guestsRepository.findAllByBoardsIdOrderByUpdatedAtDesc(boardsId);
        List<GoodsDonation> goodsDonationList = goodsDonationRepository.findAllByBoardsIdWithUser(boardsId);

        List<AttendanceUserInfoDto> attendance = guestList.stream().map(AttendanceUserInfoDto::from)
                .collect(Collectors.toList());

        List<DonationUserInfoDto> donation = goodsDonationList.stream().map(DonationUserInfoDto::from)
                .collect(Collectors.toList());

        return AlarmListResponseDto.from(attendance, donation);
    }


}
