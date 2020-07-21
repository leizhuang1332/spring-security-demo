package com.lz.security.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan("com.lz.security.mapper")
public class MybatisConfig {

}
