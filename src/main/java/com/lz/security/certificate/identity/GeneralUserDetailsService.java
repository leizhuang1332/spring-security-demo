package com.lz.security.certificate.identity;

import com.lz.security.entity.RoleEntity;
import com.lz.security.entity.UserEntity;
import com.lz.security.service.RoleService;
import com.lz.security.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
public class GeneralUserDetailsService implements UserDetailsService {

    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String credentials) throws UsernameNotFoundException {
        log.info("登陆凭证 --- {}", credentials);
        UserEntity user = null;
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        String[] credential = credentials.split("::");
        switch (credential[0]) {
            case "formLogin":
                // 1.查询用户信息
                user = userService.getByUsername(credential[1]);
                break;
            case "verifyCode":

                break;
            case "wechatLogin":

                break;
            default:
        }
        if (user != null) {
            // 2.获取用户角色
            List<RoleEntity> roles = roleService.getByUserId(user.getId());
            log.info("用户角色 --- {}, {}", user.getUsername(), roles.stream().map(RoleEntity::getName).collect(Collectors.toList()));
            // 3.声明用户授权
            roles.forEach(role -> {
                if (role != null && role.getEnname() != null) {
                    GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(role.getEnname());
                    grantedAuthorities.add(grantedAuthority);
                }
            });
            // 由框架完成认证工作
            return new User(user.getUsername(), passwordEncoder.encode(user.getPassword()), grantedAuthorities);
        }
        throw new UsernameNotFoundException(credential[1]);
    }
}
