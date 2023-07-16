package com.wedding.serviceapi.admin.dto.summary;

import lombok.*;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AttendanceSummaryDto {
    private int total;
    private int yes;
    private long yesRate;
    private int no;
    private long noRate;
    private int unknown;
    private long unknownRate;

    public static AttendanceSummaryDto emptyGuests() {
        return new AttendanceSummaryDto();
    }

    public static AttendanceSummaryDto of(int total, int yes, long yesRate, int no, long noRate, int unknown, long unknownRate) {
        return new AttendanceSummaryDto(total, yes, yesRate, no, noRate, unknown, unknownRate);
    }
}
