package com.lz.security.certificate.identity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

@Setter
@Getter
public class CustomUserDetails extends User {

    private String loginType;

    public CustomUserDetails(String username, String password,
                             Collection<? extends GrantedAuthority> authorities) {
        super(username, password, true, true, true, true, authorities);
    }

    public CustomUserDetails(String username, String password, boolean enabled,
                             boolean accountNonExpired, boolean credentialsNonExpired,
                             boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
    }
}
