package com.lz.security.certificate.jwt;

import java.util.ArrayList;
import java.util.List;

public class JwtSubject {
    private String loginType;
    private String credentials;
    private List<String> roles = new ArrayList<>();

    public String getLoginType() {
        return loginType;
    }

    public void setLoginType(String loginType) {
        this.loginType = loginType;
    }

    public String getCredentials() {
        return credentials;
    }

    public void setCredentials(String credentials) {
        this.credentials = credentials;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    @Override
    public String toString() {
        return "JwtSubject{" +
                "loginType='" + loginType + '\'' +
                ", credentials='" + credentials + '\'' +
                ", roles=" + roles +
                '}';
    }
}
