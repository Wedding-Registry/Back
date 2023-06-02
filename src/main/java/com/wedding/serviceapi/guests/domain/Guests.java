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

    public void changeAttendanceType(AttendanceType attendanceType) {
        // TODO: 2023/06/02 참석 여부 수정이 yes/no/unknown 형태가 아닌 다른 형태로 올때 에러처리 필요
        this.attendance = attendanceType;
    }
}
