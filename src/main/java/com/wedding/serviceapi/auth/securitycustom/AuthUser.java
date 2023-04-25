package com.wedding.serviceapi.auth.securitycustom;

import com.wedding.serviceapi.users.domain.Role;
import com.wedding.serviceapi.users.domain.Users;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static com.wedding.serviceapi.users.domain.Role.USER;

@Getter
public class AuthUser extends User {

    private Users users;
    private Long userId;
    private String userName;

    public AuthUser(Users users) {
        super(users.getEmail(), users.getPassword(), new ArrayList<>(List.of(new SimpleGrantedAuthority(users.getRole().name()))));
        this.users = users;
        this.userId = users.getId();
        this.userName = users.getName();
    }

    public AuthUser(Long userId, String userName, Role role) {
        super(userName, userName, new ArrayList<>(List.of(new SimpleGrantedAuthority(role.name()))));
        this.users = Users.builder().id(userId).name(userName).role(role).build();
        this.userId = userId;
        this.userName = userName;
    }

    public AuthUser() {
        super(null, null, new ArrayList<>());
    }
}
