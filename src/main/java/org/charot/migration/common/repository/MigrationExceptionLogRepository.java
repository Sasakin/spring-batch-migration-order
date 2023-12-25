package org.charot.migration.common.repository;

import org.charot.migration.common.sql.SQLQueries;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.support.TransactionTemplate;

import javax.sql.DataSource;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class MigrationExceptionLogRepository {

    private final JdbcTemplate jdbcTemplate;

    private final TransactionTemplate transactionTemplate;

    private final AtomicBoolean tableNotCreated = new AtomicBoolean(true);

    public MigrationExceptionLogRepository(DataSource dataSource, TransactionTemplate transactionTemplate) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.transactionTemplate = transactionTemplate;
    }

    public void writeReadErrorLogInTransaction(String msgId, String type, String message) {
        transactionTemplate.execute(status -> writeReadErrorLog(msgId, type, message));
    }

    public void writeReadErrorLogInTransaction(List<String> msgIds, String type, String message) {
        transactionTemplate.execute(status -> writeReadErrorLog(msgIds, type, message));
    }

    Object writeReadErrorLog(String msgId, String type, String message) {
        if (tableNotCreated.get()) {
            createMigrationErrorLogsTable();
        }
        Date currentDate = new Date(System.currentTimeMillis());
        insertOrUpdateErrorLog(msgId, currentDate, type, message);
        return null;
    }

    Object writeReadErrorLog(List<String> msgIds, String type, String message) {
        if (tableNotCreated.get()) {
            createMigrationErrorLogsTable();
        }
        Date currentDate = new Date(System.currentTimeMillis());
        for (String msgId : msgIds) {
            insertOrUpdateErrorLog(msgId, currentDate, type, message);
        }
        return null;
    }

    void createMigrationErrorLogsTable() {
        jdbcTemplate.execute(SQLQueries.CREATE_MIGRATION_ERROR_LOGS_TABLE);
        tableNotCreated.set(false);
    }

    private void insertOrUpdateErrorLog(String msgId, Date currentDate, String type, String message) {
        jdbcTemplate.update(SQLQueries.INSERT_OR_UPDATE_ERROR_LOG, msgId, currentDate, type, message, currentDate, type, message);
    }
}
