package com.wedding.serviceapi.guests.domain;

import com.wedding.serviceapi.boards.domain.Boards;
import com.wedding.serviceapi.common.domain.BaseEntity;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class AccountTransfer extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "account_transfer_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "boards_id")
    private Boards boards;

    private String transferMemo;

    public void changeMemo(String transferMemo) {
        this.transferMemo = transferMemo;
    }
}
