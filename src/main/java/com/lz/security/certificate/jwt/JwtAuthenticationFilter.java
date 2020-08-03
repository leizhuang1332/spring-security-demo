package com.lz.security.certificate.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lz.security.certificate.identity.CustomUserDetails;
import com.lz.security.certificate.identity.GeneralAuthenticationToken;
import com.lz.security.util.BeanMapUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.util.matcher.RequestHeaderRequestMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private RequestHeaderRequestMatcher requiresAuthenticationRequestMatcher;

    public JwtAuthenticationFilter() {
        //拦截header中带Authorization的请求
        this.requiresAuthenticationRequestMatcher = new RequestHeaderRequestMatcher("Authorization");
    }

    private String getJwtToken(HttpServletRequest request) {
        String authInfo = request.getHeader("Authorization");
        return authInfo.replace("Bearer ", "");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            if (!requiresAuthentication(request)) {
                throw new AuthenticationServiceException("The request header needs to contain 'Authorization' information");
            }
            //从头中获取token并封装后提交给AuthenticationManager
            String token = getJwtToken(request);
            if (!StringUtils.isEmpty(token)) {
                Claims claimsFromToken;
                try {
                    claimsFromToken = JwtUtils.getInfoFromToken(token, RsaUtils.getPublicKey());
                } catch (ExpiredJwtException e) {
                    throw new AuthenticationServiceException("The certification is invalid");
                } catch (Exception e) {
                    throw new AuthenticationServiceException(e.getMessage());
                }
                if (claimsFromToken != null) {
                    Map SUBJECT = (Map) claimsFromToken.get(Claims.SUBJECT);
                    JwtSubject jwtSubject;
                    try {
                        jwtSubject = BeanMapUtils.mapToBean(SUBJECT, JwtSubject.class);
                        System.out.println(jwtSubject);
                    } catch (Exception e) {
                        throw new AuthenticationServiceException(e.getMessage());
                    }
                    List<GrantedAuthority> grantedAuthorities = jwtSubject.getRoles().stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
                    CustomUserDetails userDetails = new CustomUserDetails(jwtSubject.getCredentials(), "", grantedAuthorities);
                    userDetails.setLoginType(jwtSubject.getLoginType());

                    GeneralAuthenticationToken authentication = new GeneralAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            } else {
                throw new AuthenticationServiceException("The request header needs to contain 'Authorization' information");
            }
        } catch (AuthenticationException e) {
            unsuccessfulAuthentication(request, response, e);
            return;
        }
        filterChain.doFilter(request, response);
    }

    private boolean requiresAuthentication(HttpServletRequest request) {
        return requiresAuthenticationRequestMatcher.matches(request);
    }

    private void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        response.setContentType("application/json;charset=utf-8");
        PrintWriter out = response.getWriter();
        Map<String, Object> map = new HashMap<>();
        map.put("code", HttpServletResponse.SC_UNAUTHORIZED);
        map.put("msg", exception.getMessage());
        out.write(new ObjectMapper().writeValueAsString(map));
        out.flush();
        out.close();
    }
}