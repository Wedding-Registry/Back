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
    private List<HusbandWifeNameDto> users = new ArrayList<>();
    private List<HusbandWifeBankAccountDto> account = new ArrayList<>();
    private String location;
    private String weddingDate;
    private String weddingTime;

    public WeddingHallInfoDto(Boards boards) {
        extractNameAndBankAccountForSettingUsersAndAccount(boards);
        this.location = boards.getAddress();
        this.weddingDate = boards.getDate();
        this.weddingTime = boards.getTime();
    }

    private void extractNameAndBankAccountForSettingUsersAndAccount(Boards boards) {
        HusbandAndWifeEachInfo husband = boards.getHusband();
        HusbandAndWifeEachInfo wife = boards.getWife();
        System.out.println("husband : " + husband);
        users.add(new HusbandWifeNameDto("husband", husband.getName()));
        users.add(new HusbandWifeNameDto("wife", wife.getName()));

        account.add(new HusbandWifeBankAccountDto("husband", husband.getBank(), husband.getAccount()));
        account.add(new HusbandWifeBankAccountDto("wife", wife.getBank(), wife.getAccount()));
    }
}
