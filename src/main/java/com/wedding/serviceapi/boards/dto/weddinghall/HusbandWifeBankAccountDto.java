package com.wedding.serviceapi.boards.dto.weddinghall;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
public class HusbandWifeBankAccountDto {
    private String gender;
    private String name;
    private String bank;
    private String account;
}
