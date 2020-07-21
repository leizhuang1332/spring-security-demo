package com.lz.security.entity;

import lombok.Data;

@Data
public class UserEntity {

    private Long id;
    private String username;
    private String password;
    private String phone;
    private String email;
    private String created;
    private String updated;
}
