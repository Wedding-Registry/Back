package com.wedding.serviceapi.auth.vo;

public interface LoginRequestVo {

    default String getSocialIdOrEmail() {
        return null;
    }

    default String getPassword() {
        return null;
    }

    default String getSocialId() {
        return null;
    }
}
