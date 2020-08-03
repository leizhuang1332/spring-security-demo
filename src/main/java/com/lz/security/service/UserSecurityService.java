package com.lz.security.service;

import com.lz.security.entity.UserSecurityEntity;
import com.lz.security.entity.inteface.UserSecurityEntityInterface;
import com.lz.security.service.inteface.UserSecurityInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Service
public class UserSecurityService implements UserSecurityInterface {

    @Autowired
    @Qualifier("jdbcTemplate")
    private JdbcTemplate jdbcTemplate;

    @Override
    public UserSecurityEntity getByUsername(String username) {

        String sql = "SELECT * FROM tb_user WHERE username = ? ";

        List<UserSecurityEntity> query = jdbcTemplate.query(sql, new Object[]{username}, new UserRowMapper());

        return query.isEmpty() ? null : query.get(0);
    }

    @Override
    public UserSecurityEntityInterface getByOpenid(String openid) {
        String sql = "SELECT * FROM tb_user WHERE openid = ? ";


        List<UserSecurityEntity> query = jdbcTemplate.query(sql, new Object[]{openid}, new UserRowMapper());

        return query.isEmpty() ? null : query.get(0);
    }

    class UserRowMapper implements RowMapper<UserSecurityEntity> {

        @Override
        public UserSecurityEntity mapRow(ResultSet rs, int rowNum) throws SQLException {

            UserSecurityEntity userEntity = new UserSecurityEntity();
            userEntity.setId(rs.getLong("id"));
            userEntity.setUsername(rs.getString("username"));
            userEntity.setPassword(rs.getString("password"));
            userEntity.setPhone(rs.getString("phone"));
            userEntity.setEmail(rs.getString("email"));
            userEntity.setCreated(rs.getString("created"));
            userEntity.setUpdated(rs.getString("updated"));

            return userEntity;
        }
    }
}
