package com.lz.security.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ConfigurationProperties(prefix = "security.datasource")
@PropertySource("classpath:security.properties")
public class DatasourceProperty {

    private String driverClassName;
    private String url;
    private String username;
    private String password;
    private String type;
    public static String websiteAppid;
    public static String websiteSecret;

    public String getDriverClassName() {
        return driverClassName;
    }

    public void setDriverClassName(String driverClassName) {
        this.driverClassName = driverClassName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getWebsiteAppid() {
        return websiteAppid;
    }

    @Value("${security.datasource.website.appid}")
    public void setWebsiteAppid(String websiteAppid) {
        DatasourceProperty.websiteAppid = websiteAppid;
    }

    public String getWebsiteSecret() {
        return websiteSecret;
    }

    @Value("${security.datasource.website.secret}")
    public void setWebsiteSecret(String websiteSecret) {
        DatasourceProperty.websiteSecret = websiteSecret;
    }
}
