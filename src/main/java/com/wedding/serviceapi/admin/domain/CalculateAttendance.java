package com.wedding.serviceapi.admin.domain;

import com.wedding.serviceapi.admin.dto.summary.AttendanceSummaryDto;
import com.wedding.serviceapi.guests.domain.AttendanceType;
import com.wedding.serviceapi.guests.domain.Guests;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CalculateAttendance {

    public AttendanceSummaryDto makeAttendanceSummary(List<Guests> guestsList) {
        int total = guestsList.size();
        if (total == 0) {
            return AttendanceSummaryDto.emptyGuests();
        }

        int yes = 0;
        int no = 0;
        int unknown = 0;

        for (Guests guest : guestsList) {
            if (guest.getAttendance() == AttendanceType.YES) {
                yes += 1;
            } else if (guest.getAttendance() == AttendanceType.NO) {
                no += 1;
            } else {
                unknown += 1;
            }
        }

        long yesRate = Math.round((yes * 100.0 / total));
        long noRate = Math.round((no * 100.0 / total));
        long unKnownRate = Math.round((unknown * 100.0 / total));
        return AttendanceSummaryDto.of(total, yes, yesRate, no, noRate, unknown, unKnownRate);
    }
}
