package com.wedding.serviceapi.admin.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AccountTransferRequestVo {
    @NotBlank(message = "메모를 추가해 주세요")
    private String transferMemo;
}
