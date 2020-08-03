package com.lz.security.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lz.security.certificate.authority.CustomAccessDecisionManager;
import com.lz.security.certificate.authority.CustomSecurityMetadataSource;
import com.lz.security.certificate.handler.*;
import com.lz.security.certificate.identity.GeneralAuthenticationFilter;
import com.lz.security.certificate.identity.GenralAuthenticationProvider;
import com.lz.security.certificate.jwt.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.session.ConcurrentSessionFilter;

import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    CustomSecurityMetadataSource CustomSecurityMetadataSource;
    @Autowired
    CustomAccessDecisionManager customAccessDecisionManager;
    @Autowired
    GenralAuthenticationProvider genralAuthenticationProvider;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(genralAuthenticationProvider);
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/css/**", "/js/**", "/index.html", "/img/**", "/fonts/**", "/favicon.ico", "/verifyCode");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .withObjectPostProcessor(new ObjectPostProcessor<FilterSecurityInterceptor>() {
                    @Override
                    public <O extends FilterSecurityInterceptor> O postProcess(O object) {
                        object.setAccessDecisionManager(customAccessDecisionManager);
                        object.setSecurityMetadataSource(CustomSecurityMetadataSource);
                        return object;
                    }
                })
                .and()
                .logout()
                .logoutSuccessHandler(new CustomLogoutSuccessHandler())
                .permitAll()
                .and()
                .csrf().disable()
                .exceptionHandling()
                // 没有认证时，在这里处理结果，不要重定向
                .authenticationEntryPoint(new CustomAuthenticationEntryPoint())
                // 权限决策异常处理
                .accessDeniedHandler(new CustomAccessDeniedHandler());

        http.addFilterBefore(new ConcurrentSessionFilter(sessionRegistry(), event -> {
            HttpServletResponse resp = event.getResponse();
            resp.setContentType("application/json;charset=utf-8");
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            PrintWriter out = resp.getWriter();
            Map<String, Object> map = new HashMap<>();
            map.put("code", HttpServletResponse.SC_UNAUTHORIZED);
            map.put("msg", "您已在另一台设备登录，本次登录已下线!");
            out.write(new ObjectMapper().writeValueAsString(map));
            out.flush();
            out.close();
        }), ConcurrentSessionFilter.class);

        http.addFilterBefore(generalAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
        http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    public GeneralAuthenticationFilter generalAuthenticationFilter() throws Exception {
        GeneralAuthenticationFilter generalAuthenticationFilter = new GeneralAuthenticationFilter();
        generalAuthenticationFilter.setFilterProcessesUrl("/login");
        generalAuthenticationFilter.setAuthenticationManager(authenticationManagerBean());
        generalAuthenticationFilter.setAuthenticationSuccessHandler(new CustomAuthenticationSuccessHandler());
        generalAuthenticationFilter.setAuthenticationFailureHandler(new CustomAuthenticationFailureHandler());
        return generalAuthenticationFilter;
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter();
    }

    @Bean
    public SessionRegistryImpl sessionRegistry() {
        return new SessionRegistryImpl();
    }
}
