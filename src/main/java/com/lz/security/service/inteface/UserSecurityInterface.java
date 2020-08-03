package com.lz.security.service.inteface;

import com.lz.security.entity.inteface.UserSecurityEntityInterface;

public interface UserSecurityInterface {

    UserSecurityEntityInterface getByUsername(String username);

    UserSecurityEntityInterface getByOpenid(String openid);
}
