package com.lz.security.certificate.identity;

import com.lz.security.AuthenticationAdapter;
import com.lz.security.entity.inteface.RoleEntityInterface;
import com.lz.security.entity.inteface.UserSecurityEntityInterface;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class GeneralUserDetailsService implements UserDetailsService {

    private Logger log = LoggerFactory.getLogger(GeneralUserDetailsService.class);

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String credentials) throws UsernameNotFoundException {
        log.info("登陆凭证 --- {}", credentials);
        UserSecurityEntityInterface user;
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        String[] credential = credentials.split("::");
        switch (credential[0]) {
            case "formLogin":
                // 1.查询用户信息
                user = AuthenticationAdapter.getInstance().getUserService().getByUsername(credential[1]);
                return buildUserDetails(user, grantedAuthorities, credential, "formLogin");
            case "verifyCode":

                break;
            case "wechatLogin":
                user = AuthenticationAdapter.getInstance().getUserService().getByOpenid(credential[1]);
                return buildUserDetails(user, grantedAuthorities, credential, "wechatLogin");
        }
        throw new UsernameNotFoundException("User [" + credential[1] + "] identity is abnormal, please check and try again! ");
    }

    private CustomUserDetails buildUserDetails(UserSecurityEntityInterface user, List<GrantedAuthority> grantedAuthorities, String[] credential, String loginType) {
        if (user != null) {
            // 2.获取用户角色
            List<? extends RoleEntityInterface> roles = AuthenticationAdapter.getInstance().getRoleService().getByUserId(user.getId());
            log.info("用户角色 --- {}, {}", user.getUsername(), roles.stream().map(RoleEntityInterface::getName).collect(Collectors.toList()));
            // 3.声明用户授权
            roles.forEach(role -> {
                if (role != null && role.getEnname() != null) {
                    GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(role.getEnname());
                    grantedAuthorities.add(grantedAuthority);
                }
            });
            // 由框架完成认证工作
            CustomUserDetails userDetails = new CustomUserDetails(credential[1], loginType.equals("formLogin") ? passwordEncoder.encode(user.getPassword()) : "", grantedAuthorities);
            userDetails.setLoginType(credential[0]);
            return userDetails;
        }
        throw new UsernameNotFoundException("User [" + credential[1] + "] No roles are bound, please contact your administrator! ");
    }
}
