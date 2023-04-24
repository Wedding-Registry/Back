package com.wedding.serviceapi.auth;

import com.wedding.serviceapi.users.domain.Users;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.ArrayList;
import java.util.Collection;

@Getter
public class AuthUser extends User {

    private Long userId;
    private String userName;

    public AuthUser(Users users) {
        super(users.getEmail(), users.getPassword(), new ArrayList<>());
        this.userId = users.getId();
        this.userName = users.getName();
    }

    public AuthUser(Long userId, String userName) {
        super(userName, userName, new ArrayList<>());
        this.userId = userId;
        this.userName = userName;
    }

    public AuthUser() {
        super(null, null, new ArrayList<>());
    }
}
