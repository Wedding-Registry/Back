package com.wedding.serviceapi.boards.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;

@Embeddable
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class HusbandAndWifeEachInfo {

    private String name;
    private String bank;
    private String account;

    public void changeName(String name) {
        if (name.isBlank()) throw new IllegalArgumentException("이름은 필수입니다.");
        this.name = name.trim();
    }

    public void changeBank(String bank) {
        if (bank.isBlank()) throw new IllegalArgumentException("은행명은 필수입니다.");
        this.bank = bank.trim();
    }

    public void changeAccount(String account) {
        if (account.isBlank()) throw new IllegalArgumentException("계좌번호는 필수입니다.");
        this.account = account.trim();
    }
}
