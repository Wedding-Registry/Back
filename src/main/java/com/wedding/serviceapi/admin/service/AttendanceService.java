package com.wedding.serviceapi.admin.service;

import com.wedding.serviceapi.admin.domain.CalculateAttendance;
import com.wedding.serviceapi.admin.dto.attendance.AttendanceResponseDto;
import com.wedding.serviceapi.guests.domain.AttendanceType;
import com.wedding.serviceapi.guests.domain.Guests;
import com.wedding.serviceapi.guests.repository.GuestsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AttendanceService {

    private final GuestsRepository guestsRepository;
    private final CalculateAttendance calculateAttendance;

    public AttendanceResponseDto getAllAttendanceInfo(Long boardsId) {
        List<Guests> guestsList = guestsRepository.findAllByBoardsIdWithUsers(boardsId);
        return calculateAttendance.makeAttendanceResponse(guestsList);
    }

    @Transactional
    public void changeAttendance(Long usersId, Long boardsId, AttendanceType attendanceType) {
        Guests guests = guestsRepository.findByUsersIdAndBoardsId(usersId, boardsId).orElseThrow(() -> new NoSuchElementException("해당하는 게스트가 없습니다."));
        guests.changeAttendanceType(attendanceType);
    }
}
