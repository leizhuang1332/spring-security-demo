package com.lz.security.mapper;

import com.lz.security.entity.PermissionEntity;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PermissionMapper {
    List<PermissionEntity> getByUserId(@Param("userId") Long userId);
}
