package com.lz.security;

import com.lz.security.entity.inteface.RolePermissionEntityInterface;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 启动加载器
 * 加载需要的初始化数据
 */
@Component
@Order(100)
public class Bootstrapper implements CommandLineRunner {

    private Logger log = LoggerFactory.getLogger(Bootstrapper.class);

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
        List<? extends RolePermissionEntityInterface> all = AuthenticationAdapter.getInstance().getRolePermissionService().getAll();
        Map<String, List<RolePermissionEntityInterface>> collect = all.stream().collect(Collectors.groupingBy(RolePermissionEntityInterface::getPermissionUrl));
        for (Map.Entry<String, List<RolePermissionEntityInterface>> entry : collect.entrySet()) {

            String key = entry.getKey();
            List<RolePermissionEntityInterface> value = entry.getValue();

            Collection<ConfigAttribute> configAttributes = new ArrayList<>();
            for (RolePermissionEntityInterface rolePermissionEntityInterface : value) {
                ConfigAttribute configAttribute = new SecurityConfig(rolePermissionEntityInterface.getRoleEnname());
                configAttributes.add(configAttribute);
            }
            ROLE_AUTHORITY_MAP.put(key, configAttributes);
        }
        log.info("初始化加载授权 --- {}", ROLE_AUTHORITY_MAP);
    }
}
