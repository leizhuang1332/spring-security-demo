package com.lz.security.certificate.identity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.SpringSecurityMessageSource;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class GenralAuthenticationProvider implements AuthenticationProvider {

    protected MessageSourceAccessor messages = SpringSecurityMessageSource.getAccessor();

    @Autowired
    private GeneralUserDetailsService userDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        GeneralAuthenticationToken generalAuthenticationToken = (GeneralAuthenticationToken) authentication;

        Object principal = generalAuthenticationToken.getPrincipal();
        Object credentials = generalAuthenticationToken.getCredentials();
        Object details = generalAuthenticationToken.getDetails();

        String loginType = generalAuthenticationToken.getLoginType();

        UserDetails userDetails = userDetailsService.loadUserByUsername(loginType + "::" + principal);

        if ("formLogin".equals(loginType))
            additionalAuthenticationChecks(userDetails, generalAuthenticationToken);

        GeneralAuthenticationToken result = new GeneralAuthenticationToken(userDetails, credentials, userDetails.getAuthorities());
        result.setDetails(details);

        return result;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return (GeneralAuthenticationToken.class.isAssignableFrom(authentication));
    }

    private void additionalAuthenticationChecks(UserDetails userDetails,
                                                GeneralAuthenticationToken authentication)
            throws AuthenticationException {
        if (authentication.getCredentials() == null) {

            throw new BadCredentialsException(messages.getMessage(
                    "AbstractUserDetailsAuthenticationProvider.badCredentials",
                    "Bad credentials"));
        }

        String presentedPassword = authentication.getCredentials().toString();

        if (!passwordEncoder.matches(presentedPassword, userDetails.getPassword())) {

            throw new BadCredentialsException(messages.getMessage(
                    "AbstractUserDetailsAuthenticationProvider.badCredentials",
                    "Bad credentials"));
        }
    }
}
