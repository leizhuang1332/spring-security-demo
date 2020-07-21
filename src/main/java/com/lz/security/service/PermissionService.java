package com.lz.security.service;

import com.lz.security.entity.PermissionEntity;
import com.lz.security.mapper.PermissionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PermissionService {

    @Autowired
    private PermissionMapper permissionMapper;

    public List<PermissionEntity> getByUserId(Long id) {
        return permissionMapper.getByUserId(id);
    }
}
