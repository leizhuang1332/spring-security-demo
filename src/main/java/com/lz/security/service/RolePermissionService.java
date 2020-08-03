package com.lz.security.service;

import com.lz.security.entity.RolePermissionEntity;
import com.lz.security.service.inteface.RolePermissionInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Service
public class RolePermissionService implements RolePermissionInterface {

    @Autowired
    @Qualifier("jdbcTemplate")
    private JdbcTemplate jdbcTemplate;

    @Override
    public List<RolePermissionEntity> getAll() {

        String sql = "SELECT\n" +
                "            r.id AS roleId,\n" +
                "            r.`name` AS roleName,\n" +
                "            r.enname AS roleEnname,\n" +
                "            p.id AS permissionId,\n" +
                "            p.`name` AS permissionName,\n" +
                "            p.enname AS permissionEnName,\n" +
                "            p.url AS permissionUrl\n" +
                "        FROM\n" +
                "            tb_role AS r\n" +
                "            INNER JOIN tb_role_permission AS rp ON r.id = rp.role_id\n" +
                "            INNER JOIN tb_permission AS p ON p.id = rp.permission_id";

        return jdbcTemplate.query(sql, new RolePermissionRowMapper());
    }

    class RolePermissionRowMapper implements RowMapper<RolePermissionEntity> {

        @Override
        public RolePermissionEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
            RolePermissionEntity rolePermissionEntity = new RolePermissionEntity();
            rolePermissionEntity.setRoleId(rs.getLong("roleId"));
            rolePermissionEntity.setRoleName(rs.getString("roleName"));
            rolePermissionEntity.setRoleEnname(rs.getString("roleEnname"));
            rolePermissionEntity.setPermissionId(rs.getLong("permissionId"));
            rolePermissionEntity.setPermissionName(rs.getString("permissionName"));
            rolePermissionEntity.setPermissionEnName(rs.getString("permissionEnName"));
            rolePermissionEntity.setPermissionUrl(rs.getString("permissionUrl"));
            return rolePermissionEntity;
        }
    }
}
