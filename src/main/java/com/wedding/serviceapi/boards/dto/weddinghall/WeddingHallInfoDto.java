package com.wedding.serviceapi.boards.dto.weddinghall;

import com.wedding.serviceapi.boards.domain.Boards;
import com.wedding.serviceapi.boards.domain.HusbandAndWifeEachInfo;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter @Setter
@NoArgsConstructor
public class WeddingHallInfoDto {
    private List<HusbandWifeBankAccountDto> account = new ArrayList<>();
    private String location;
    private String weddingDate;
    private String weddingTime;

    public WeddingHallInfoDto(Boards boards) {
        extractNameAndBankAccountForSettingAccount(boards);
        this.location = boards.getAddress();
        this.weddingDate = boards.getDate();
        this.weddingTime = boards.getTime();
    }

    private void extractNameAndBankAccountForSettingAccount(Boards boards) {
        HusbandAndWifeEachInfo husband = boards.getHusband();
        HusbandAndWifeEachInfo wife = boards.getWife();
        this.account.add(new HusbandWifeBankAccountDto("husband", husband.getName(), husband.getBank(), husband.getAccount()));
        this.account.add(new HusbandWifeBankAccountDto("wife", wife.getName(), wife.getBank(), wife.getAccount()));
    }
}
