package com.lz.security;

import com.lz.security.entity.RolePermissionEntity;
import com.lz.security.service.RolePermissionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 启动加载器
 * 加载需要的初始化数据
 */
@Slf4j
@Component
public class Bootstrapper implements CommandLineRunner {

    @Autowired
    private RolePermissionService rolePermissionService;

    /**
     * 角色权限
     * <权限: Collection<角色>>
     */
    public static Map<String, Collection<ConfigAttribute>> ROLE_AUTHORITY_MAP = new HashMap<>();

    @Override
    public void run(String... args) throws Exception {
        loadAuthority();
    }

    /**
     * 加载授权
     */
    public void loadAuthority() {
        // 查询角色权限表
        List<RolePermissionEntity> all = rolePermissionService.getAll();
        Map<String, List<RolePermissionEntity>> collect = all.stream().collect(Collectors.groupingBy(RolePermissionEntity::getPermissionUrl));
        for (Map.Entry<String, List<RolePermissionEntity>> entry : collect.entrySet()) {

            String key = entry.getKey();
            List<RolePermissionEntity> value = entry.getValue();

            Collection<ConfigAttribute> configAttributes = new ArrayList<>();
            for (RolePermissionEntity rolePermissionEntity : value) {
                ConfigAttribute configAttribute = new SecurityConfig(rolePermissionEntity.getRoleEnname());
                configAttributes.add(configAttribute);
            }
            ROLE_AUTHORITY_MAP.put(key, configAttributes);
        }
        log.info("初始化加载授权 --- {}", ROLE_AUTHORITY_MAP);
    }
}
