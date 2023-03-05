package com.wedding.serviceapi.users.domain;

import com.wedding.serviceapi.common.domain.BaseEntity;
import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
public class Users extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "users_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Users users;

    private String email;

    private String password;

    private Boolean alarmEvent;

    @Enumerated(value = EnumType.STRING)
    private LoginType loginType;

}
