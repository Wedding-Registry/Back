package com.wedding.serviceapi.admin.service;

import com.amazonaws.services.kms.model.transform.CreateGrantResultJsonUnmarshaller;
import com.wedding.serviceapi.admin.domain.CalculateAttendance;
import com.wedding.serviceapi.admin.dto.attendance.AttendanceResponseDto;
import com.wedding.serviceapi.guests.domain.AttendanceType;
import com.wedding.serviceapi.guests.domain.Guests;
import com.wedding.serviceapi.guests.repository.GuestsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.apache.bcel.generic.RET;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.stream.Collectors;

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
    public AttendanceResponseDto changeAttendance(Long usersId, Long boardsId, AttendanceType attendanceType) {
        List<Guests> guestsList = guestsRepository.findAllByBoardsIdWithUsers(boardsId).stream().peek(guests -> {
            if (Objects.equals(guests.getUsers().getId(), usersId)) {
                log.info("test");
                guests.changeAttendanceType(attendanceType);
            }
        }).collect(Collectors.toList());

        return calculateAttendance.makeAttendanceResponse(guestsList);
    }
}
