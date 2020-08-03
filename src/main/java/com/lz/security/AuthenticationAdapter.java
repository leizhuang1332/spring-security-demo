package com.lz.security;

import com.lz.security.service.inteface.RoleInterface;
import com.lz.security.service.inteface.RolePermissionInterface;
import com.lz.security.service.inteface.UserSecurityInterface;

public class AuthenticationAdapter {

    private static AuthenticationAdapter authenticationAdapter;

    private final static Object lock = new Object();

    private AuthenticationAdapter() {
    }

    private RolePermissionInterface rolePermissionService;

    public AuthenticationAdapter rolePermissionService(RolePermissionInterface rolePermissionService) {
        this.rolePermissionService = rolePermissionService;
        return this;
    }

    public RolePermissionInterface getRolePermissionService() {
        return this.rolePermissionService;
    }

    private UserSecurityInterface userService;

    public AuthenticationAdapter userService(UserSecurityInterface userService) {
        this.userService = userService;
        return this;
    }

    public UserSecurityInterface getUserService() {
        return this.userService;
    }

    private RoleInterface roleService;

    public AuthenticationAdapter roleService(RoleInterface roleService) {
        this.roleService = roleService;
        return this;
    }

    public RoleInterface getRoleService() {
        return this.roleService;
    }

    public static AuthenticationAdapter getInstance() {
        if (authenticationAdapter == null) {
            synchronized (lock) {
                if (authenticationAdapter == null) {
                    authenticationAdapter = new AuthenticationAdapter();
                }
            }
        }
        return authenticationAdapter;
    }
}
