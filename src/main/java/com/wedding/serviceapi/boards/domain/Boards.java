package com.wedding.serviceapi.boards.domain;

import com.wedding.serviceapi.common.domain.BaseEntity;
import com.wedding.serviceapi.users.domain.Users;
import lombok.Getter;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@DynamicInsert
@Getter
public class Boards extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "boards_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "users_id")
    private Users users;

    private String uuidFirst;
    private String uuidSecond;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "name", column = @Column(name = "husband_name")),
            @AttributeOverride(name = "bank", column = @Column(name = "husband_bank")),
            @AttributeOverride(name = "account", column = @Column(name = "husband_account"))
    })
    private HusbandAndWifeEachInfo husband;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "name", column = @Column(name = "wife_name")),
            @AttributeOverride(name = "bank", column = @Column(name = "wife_bank")),
            @AttributeOverride(name = "account", column = @Column(name = "wife_account"))
    })
    private HusbandAndWifeEachInfo wife;

    private String address;
    @Column(name = "wedding_date")
    private LocalDate date;
    @Column(name = "wedding_time")
    private LocalTime time;
    private String boardsMemo;

}



























