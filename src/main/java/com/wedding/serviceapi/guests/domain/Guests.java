package com.wedding.serviceapi.guests.domain;

import com.wedding.serviceapi.boards.domain.Boards;
import com.wedding.serviceapi.common.domain.BaseEntity;
import com.wedding.serviceapi.users.domain.Users;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Guests extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "users_id")
    private Users users;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "boards_id")
    private Boards boards;

    @Enumerated(value = EnumType.STRING)
    private AttendanceType attendance;
}
