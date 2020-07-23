package com.lz.security.certificate.identity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class GenralAuthenticationProvider implements AuthenticationProvider {
    @Autowired
    private GeneralUserDetailsService userDetailsService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        GeneralAuthenticationToken generalAuthenticationToken = (GeneralAuthenticationToken) authentication;

        Object principal = generalAuthenticationToken.getPrincipal();
        Object credentials = generalAuthenticationToken.getCredentials();
        Object details = generalAuthenticationToken.getDetails();

        String loginType = generalAuthenticationToken.getLoginType();

        UserDetails userDetails = userDetailsService.loadUserByUsername(loginType + "::" + principal);

        GeneralAuthenticationToken result = new GeneralAuthenticationToken(userDetails, credentials, userDetails.getAuthorities());
        result.setDetails(details);

        return result;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return (GeneralAuthenticationToken.class.isAssignableFrom(authentication));
    }
}
