package com.lz.security.service;

import com.lz.security.entity.RoleEntity;
import com.lz.security.service.inteface.RoleInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Service
public class RoleService implements RoleInterface {

    @Autowired
    @Qualifier("jdbcTemplate")
    private JdbcTemplate jdbcTemplate;

    @Override
    public List<RoleEntity> getByUserId(Long userId) {

        String sql = "SELECT\n" +
                "          r.*\n" +
                "        FROM\n" +
                "          tb_user AS u\n" +
                "          LEFT JOIN tb_user_role AS ur\n" +
                "            ON u.id = ur.user_id\n" +
                "          LEFT JOIN tb_role AS r\n" +
                "            ON r.id = ur.role_id\n" +
                "        WHERE u.id = ? ";

        return jdbcTemplate.query(sql, new Object[]{userId}, new RoleRowMapper());
    }

    class RoleRowMapper implements RowMapper<RoleEntity> {

        @Override
        public RoleEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
            RoleEntity roleEntity = new RoleEntity();
            roleEntity.setId(rs.getLong("id"));
            roleEntity.setParentId(rs.getLong("parent_id"));
            roleEntity.setName(rs.getString("name"));
            roleEntity.setEnname(rs.getString("enname"));
            roleEntity.setDescription(rs.getString("description"));
            roleEntity.setCreated(rs.getString("created"));
            roleEntity.setUpdated(rs.getString("updated"));
            return roleEntity;
        }
    }
}
