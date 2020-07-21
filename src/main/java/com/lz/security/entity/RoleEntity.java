package com.lz.security.entity;

import lombok.Data;

@Data
public class RoleEntity {

    private Long id;
    private Long parent_id;
    private String name;
    private String enname;
    private String description;
    private String created;
    private String updated;
}
