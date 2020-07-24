package com.lz.security.certificate.jwt;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class JwtSubject {
    private String loginType;
    private String credentials;
    private List<String> roles = new ArrayList<>();
}
