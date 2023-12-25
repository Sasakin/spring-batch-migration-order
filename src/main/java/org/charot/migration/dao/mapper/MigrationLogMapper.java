package org.charot.migration.dao.mapper;

import org.charot.migration.dao.entity.ZTestMigrationLog;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class MigrationLogMapper implements RowMapper<ZTestMigrationLog> {
    @Override
    public ZTestMigrationLog mapRow(ResultSet rs, int rowNum) throws SQLException {
        ZTestMigrationLog dto = new ZTestMigrationLog();
        dto.setMsgid(rs.getString("msgid"));
        dto.setDateSent(rs.getTimestamp("date_sent"));
        dto.setDateReceived(rs.getTimestamp("date_received"));
        dto.setStatus(rs.getString("status"));
        dto.setMessage(rs.getString("message"));
        return dto;
    }
}
