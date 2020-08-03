package com.lz.security.certificate.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        response.setContentType("application/json;charset=utf-8");
        PrintWriter out = response.getWriter();
        Map<String, Object> map = new HashMap<>();
        map.put("code", HttpServletResponse.SC_UNAUTHORIZED);
        if (exception instanceof LockedException) {
            map.put("msg", "账户被锁定，请联系管理员!");
        } else if (exception instanceof CredentialsExpiredException) {
            map.put("msg", "密码过期，请联系管理员!");
        } else if (exception instanceof AccountExpiredException) {
            map.put("msg", "账户过期，请联系管理员!");
        } else if (exception instanceof DisabledException) {
            map.put("msg", "账户被禁用，请联系管理员!");
        } else if (exception instanceof BadCredentialsException) {
            map.put("msg", "用户名或者密码输入错误，请重新输入!");
        } else {
            map.put("msg", exception.getMessage());
        }
        out.write(new ObjectMapper().writeValueAsString(map));
        out.flush();
        out.close();
    }
}
