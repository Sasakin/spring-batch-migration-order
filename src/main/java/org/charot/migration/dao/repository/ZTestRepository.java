package org.charot.migration.dao.repository;

import lombok.RequiredArgsConstructor;
import org.charot.migration.dao.entity.ZTestMigrationLog;
import org.charot.migration.dao.mapper.MigrationLogMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ZTestRepository {
    private final JdbcTemplate testJdbcTemplate;

    public Integer getCountRowsBySQLFromTest(String selectCountSQL) {
        return testJdbcTemplate.queryForObject(selectCountSQL, Integer.class);
    }

    public void clearMigrationLogsBySQL(String clearMigrationLogsSQL) {
        testJdbcTemplate.update(clearMigrationLogsSQL);
    }

    public List<ZTestMigrationLog> getAllMigrationLogsBySQL(String selectMigrationLogsSQL) {
        return testJdbcTemplate.query(selectMigrationLogsSQL, new MigrationLogMapper());
    }
}
