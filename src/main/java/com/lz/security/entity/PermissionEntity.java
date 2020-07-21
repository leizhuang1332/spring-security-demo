package com.lz.security.entity;

import lombok.Data;

@Data
public class PermissionEntity {

    private Long id;
    private Long parentId;
    private String name;
    private String enname;
    private String url;
    private String description;
    private String created;
    private String updated;
}
