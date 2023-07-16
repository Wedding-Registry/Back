package com.wedding.serviceapi.boards.vo.marriage;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@EqualsAndHashCode
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RequestPostMarriageBankAccountVo {
    private String bank;
    private String account;
}
