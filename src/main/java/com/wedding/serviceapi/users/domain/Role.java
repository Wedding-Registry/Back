package com.wedding.serviceapi.users.domain;

public enum Role {
    USER, ADMIN;

    public static Role fromString(String text) {
        for (Role role : Role.values()) {
            if (role.name().equalsIgnoreCase(text)) {
                return role;
            }
        }
        return null;
    }
}
