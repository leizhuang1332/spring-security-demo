package com.lz.security.mapper;

import com.lz.security.entity.RolePermissionEntity;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RolePermissionMapper {
    List<RolePermissionEntity> getAll();
}
