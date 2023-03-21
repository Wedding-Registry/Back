package com.wedding.serviceapi.boards.vo.weddinghall;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@EqualsAndHashCode
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RequestPostWeddingHallTimeVo {
    private String date;
    private String time;
}
