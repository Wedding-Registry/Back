package com.wedding.serviceapi.boards.domain;

import com.wedding.serviceapi.common.domain.BaseEntity;
import com.wedding.serviceapi.users.domain.Users;
import lombok.Getter;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

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
    private String date;
    @Column(name = "wedding_time")
    private String time;
    private String boardsMemo;

    // 비즈니스 메서드
    public void updateAddress(String address) {
        this.address = address;
    }

    public void updateDateAndTime(String date, String time) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

        LocalDate localDate = parseDateString(date);
        LocalTime localTime = parseTimeString(time);

        this.date = localDate.format(dateFormatter);
        this.time = localTime.format(timeFormatter);
    }

    private LocalDate parseDateString(String date) {
        // TODO: 2023/03/19 에러처리 필요
        if (date.length() != 8 || date.startsWith("0")) throw new IllegalArgumentException("잘못된 날짜 정보입니다.");

        int year = Integer.parseInt(date.substring(0, 4));
        int month = Integer.parseInt(date.substring(4, 6));
        int day = Integer.parseInt(date.substring(6));

        return LocalDate.of(year, month, day);
    }

    private LocalTime parseTimeString(String time) {
        if (time.length() != 4) throw new IllegalArgumentException("잘못된 시간 정보입니다.");
        int hour = Integer.parseInt(time.substring(0, 2));
        int minute = Integer.parseInt(time.substring(2));

        return LocalTime.of(hour, minute);
    }

}



























