package com.lz.security.mapper;

import com.lz.security.entity.UserEntity;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserMapper {

    UserEntity getByUsername(@Param("username") String username);
}
