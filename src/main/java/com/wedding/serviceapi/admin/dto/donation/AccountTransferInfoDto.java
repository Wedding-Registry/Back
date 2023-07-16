package com.wedding.serviceapi.admin.dto.donation;

import com.wedding.serviceapi.guests.domain.AccountTransfer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AccountTransferInfoDto {
    @NotNull(message = "메모 아이디가 필요합니다.")
    private Long accountTransferId;
    @NotBlank(message = "메모를 작성해 주세요")
    private String transferMemo;

    public static AccountTransferInfoDto from(AccountTransfer accountTransfer) {
        return new AccountTransferInfoDto(accountTransfer.getId(), accountTransfer.getTransferMemo());
    }
}
