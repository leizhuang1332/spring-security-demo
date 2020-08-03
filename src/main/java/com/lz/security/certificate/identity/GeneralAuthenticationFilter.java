package com.lz.security.certificate.identity;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lz.security.common.LoginHandler;
import com.lz.security.common.UserMetadata;
import com.lz.security.config.DatasourceProperty;
import com.lz.security.util.HttpUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.boot.json.JsonParser;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Component;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@Component
public class GeneralAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    @Autowired
    private LoginHandler wechatLoginHandler;

    public static final String SPRING_SECURITY_FORM_LOGINTYPE_KEY = "loginType";
    public static final String SPRING_SECURITY_FORM_USERNAME_KEY = "username";
    public static final String SPRING_SECURITY_FORM_PASSWORD_KEY = "password";
    public static final String SPRING_SECURITY_CODE_KEY = "code";
    private boolean postOnly = true;

    public GeneralAuthenticationFilter() {
        super(new AntPathRequestMatcher("/login", "POST"));
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {

        if (postOnly && !request.getMethod().equals("POST")) {
            throw new AuthenticationServiceException(
                    "Authentication method not supported: " + request.getMethod());
        }
        switch (obtainParam(request, SPRING_SECURITY_FORM_LOGINTYPE_KEY).toString().trim()) {
            case "formLogin":
                String username = obtainParam(request, SPRING_SECURITY_FORM_USERNAME_KEY).toString().trim();
                String password = obtainParam(request, SPRING_SECURITY_FORM_PASSWORD_KEY).toString().trim();

                GeneralAuthenticationToken authRequest = new GeneralAuthenticationToken(username, password);
                authRequest.setLoginType("formLogin");

                setDetails(request, authRequest);

                return this.getAuthenticationManager().authenticate(authRequest);
            case "verifyCode":

                break;
            case "wechatLogin":

//                "openid" -> "oV775s2YuoHXBUKa8sVe5XcziO34"

                String code = obtainParam(request, SPRING_SECURITY_CODE_KEY).toString().trim();

                String url1 = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=" + DatasourceProperty.websiteAppid +
                        "&secret=" + DatasourceProperty.websiteSecret +
                        "&code=" + code +
                        "&grant_type=authorization_code";

                String oauth2Result = HttpUtils.getInstance().getForm(url1);

                JsonParser jsonParser = new JacksonJsonParser();
                Map<String, Object> oauth2ResultMap = jsonParser.parseMap(oauth2Result);

                String url2 = "https://api.weixin.qq.com/sns/userinfo?access_token=" + oauth2ResultMap.get("access_token") +
                        "&openid=" + oauth2ResultMap.get("openid");

                String userInfo = HttpUtils.getInstance().getForm(url2);
                Map<String, Object> userInfoMap = jsonParser.parseMap(userInfo);

                UserMetadata.wechatUserInfo.put(userInfoMap.get("openid"), userInfoMap);

                wechatLoginHandler.handler(userInfoMap);

                GeneralAuthenticationToken wechatLoginAuthRequest = new GeneralAuthenticationToken(userInfoMap.get("openid"), "");

//                GeneralAuthenticationToken wechatLoginAuthRequest = new GeneralAuthenticationToken("oV775s2YuoHXBUKa8sVe5XcziO34", "");

                wechatLoginAuthRequest.setLoginType("wechatLogin");

                setDetails(request, wechatLoginAuthRequest);

                return this.getAuthenticationManager().authenticate(wechatLoginAuthRequest);
            case "":
                throw new AuthenticationServiceException(
                        "The parameter 'loginType' must be specified!");
            default:
                throw new AuthenticationServiceException(
                        "Unsupported login types!");
        }
        return null;
    }

    private Object obtainParam(HttpServletRequest request, String paramKay) {
        if (request.getContentType() != null && (request.getContentType().contains(MediaType.APPLICATION_JSON_VALUE) || request.getContentType().contains(MediaType.APPLICATION_JSON_UTF8_VALUE))) {
            Map loginData;
            try {
                loginData = new ObjectMapper().readValue(request.getInputStream(), Map.class);
                return loginData.get(paramKay) == null ? "" : loginData.get(paramKay);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return request.getParameter(paramKay) == null ? "" : request.getParameter(paramKay).trim();
    }

    private void setDetails(HttpServletRequest request,
                            UsernamePasswordAuthenticationToken authRequest) {
        authRequest.setDetails(authenticationDetailsSource.buildDetails(request));
    }

    @Override
    public void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        super.successfulAuthentication(request, response, chain, authResult);
    }

    @Override
    public void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        super.unsuccessfulAuthentication(request, response, failed);
    }
}
