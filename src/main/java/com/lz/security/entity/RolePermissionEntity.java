package com.lz.security.entity;

import lombok.Data;

@Data
public class RolePermissionEntity {

    private Long roleId;
    private Long permissionId;
    private String roleName;
    private String roleEnname;
    private String permissionName;
    private String permissionEnName;
    private String permissionUrl;
}
