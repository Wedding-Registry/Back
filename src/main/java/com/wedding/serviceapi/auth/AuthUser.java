package com.wedding.serviceapi.auth;

import com.wedding.serviceapi.users.domain.Users;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

@Getter
public class AuthUser extends User {

    private Long userId;
    private String userName;

    public AuthUser(Users users) {
        super(users.getEmail(), users.getPassword(), null);
        this.userId = users.getId();
        this.userName = users.getName();
    }
}
