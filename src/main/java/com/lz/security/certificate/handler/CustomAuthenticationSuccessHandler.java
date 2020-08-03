package com.lz.security.certificate.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lz.security.certificate.identity.CustomUserDetails;
import com.lz.security.certificate.jwt.JwtSubject;
import com.lz.security.certificate.jwt.JwtUtils;
import com.lz.security.certificate.jwt.RsaUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private Logger log = LoggerFactory.getLogger(CustomAuthenticationSuccessHandler.class);

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        response.setContentType("application/json;charset=utf-8");
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        String token = null;
        try {

            JwtSubject jwtSubject = new JwtSubject();
            jwtSubject.setLoginType(userDetails.getLoginType());
            jwtSubject.setCredentials(userDetails.getUsername());
            userDetails.getAuthorities().forEach(grantedAuthority -> jwtSubject.getRoles().add(grantedAuthority.getAuthority()));

            token = JwtUtils.generateTokenExpireInMinutes(jwtSubject, RsaUtils.getPrivateKey(), 60 * 24 * 7);
        } catch (Exception e) {
            log.info("TOKEN 生成失败", e);
        }
        response.setHeader("Authorization", "Bearer " + token);
        PrintWriter out = response.getWriter();
        Map<String, Object> map = new HashMap<>();
        map.put("code", HttpServletResponse.SC_OK);
        map.put("msg", "登录成功!");
        map.put("data", authentication.getPrincipal());
        out.write(new ObjectMapper().writeValueAsString(map));
        out.flush();
        out.close();
    }
}
