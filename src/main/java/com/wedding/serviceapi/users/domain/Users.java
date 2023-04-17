package com.wedding.serviceapi.users.domain;

import com.wedding.serviceapi.common.domain.BaseEntity;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
@DynamicInsert
@DynamicUpdate
@ToString
public class Users extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "users_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Users parent;

    private String email;

    private String password;

    private String name;

    private Boolean alarmEvent;

    @Enumerated(value = EnumType.STRING)
    private LoginType loginType;

    private String socialId;

    private String refreshToken;

    // 생성자 메서드
    public Users(String email, String password, LoginType loginType) {
        this.email = email;
        this.password = password;
        this.loginType = loginType;
    }

    // 비즈니스 메서드
    public void setParentId(Users parent) {
        if (Objects.isNull(this.parent)) this.parent = parent;
    }

    public void setRefreshToken(String refreshToken) {
        if (refreshToken == null) throw new IllegalArgumentException("리프레시 토큰은 필수입니다.");
        this.refreshToken = refreshToken;
    }
}
