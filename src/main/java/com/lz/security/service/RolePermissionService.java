package com.lz.security.service;

import com.lz.security.entity.RolePermissionEntity;
import com.lz.security.mapper.RolePermissionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RolePermissionService {

    @Autowired
    private RolePermissionMapper rolePermissionMapper;

    public List<RolePermissionEntity> getAll(){
        return rolePermissionMapper.getAll();
    }
}
