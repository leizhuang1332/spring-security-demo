package com.lz.security.entity;

import com.lz.security.entity.inteface.RolePermissionEntityInterface;

public class RolePermissionEntity implements RolePermissionEntityInterface {

    private Long roleId;
    private Long permissionId;
    private String roleName;
    private String roleEnname;
    private String permissionName;
    private String permissionEnName;
    private String permissionUrl;

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public Long getPermissionId() {
        return permissionId;
    }

    public void setPermissionId(Long permissionId) {
        this.permissionId = permissionId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    @Override
    public String getRoleEnname() {
        return roleEnname;
    }

    public void setRoleEnname(String roleEnname) {
        this.roleEnname = roleEnname;
    }

    public String getPermissionName() {
        return permissionName;
    }

    public void setPermissionName(String permissionName) {
        this.permissionName = permissionName;
    }

    public String getPermissionEnName() {
        return permissionEnName;
    }

    public void setPermissionEnName(String permissionEnName) {
        this.permissionEnName = permissionEnName;
    }

    @Override
    public String getPermissionUrl() {
        return permissionUrl;
    }

    public void setPermissionUrl(String permissionUrl) {
        this.permissionUrl = permissionUrl;
    }

    @Override
    public String toString() {
        return "RolePermissionEntity{" +
                "roleId=" + roleId +
                ", permissionId=" + permissionId +
                ", roleName='" + roleName + '\'' +
                ", roleEnname='" + roleEnname + '\'' +
                ", permissionName='" + permissionName + '\'' +
                ", permissionEnName='" + permissionEnName + '\'' +
                ", permissionUrl='" + permissionUrl + '\'' +
                '}';
    }
}
