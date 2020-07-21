package com.lz.security.service;

import com.lz.security.entity.RoleEntity;
import com.lz.security.mapper.RoleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleService {

    @Autowired
    private RoleMapper roleMapper;

    public List<RoleEntity> getByUserId(Long userId){
        return roleMapper.getByUserId(userId);
    }
}
