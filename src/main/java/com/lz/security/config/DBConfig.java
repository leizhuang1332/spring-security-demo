package com.lz.security.config;

import com.alibaba.druid.pool.DruidDataSourceFactory;
import com.lz.security.util.BeanMapUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.util.Map;

@Configuration
public class DBConfig {

    @javax.annotation.Resource
    private DatasourceProperty datasourceProperty;

    @Bean(name = "jdbcTemplate")
    public JdbcTemplate jdbcTemplate() throws Exception {
        Map<Object, Object> objectObjectMap = BeanMapUtils.beanToMap(datasourceProperty);
        DataSource dataSource = DruidDataSourceFactory.createDataSource(objectObjectMap);
        return new JdbcTemplate(dataSource);
    }
}
