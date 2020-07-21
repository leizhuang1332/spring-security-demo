package com.lz.security.mapper;

import com.lz.security.entity.RoleEntity;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleMapper {
    List<RoleEntity> getByUserId(@Param("userId") Long userId);
}
