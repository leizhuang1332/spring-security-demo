package com.lz.security.service;

import com.lz.security.entity.UserEntity;
import com.lz.security.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    public UserEntity getByUsername(String username){
        return userMapper.getByUsername(username);
    }
}
