package org.charot.migration.dao.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.Date;
import java.util.List;
import java.util.function.Function;

@RequiredArgsConstructor
public class ZTestMigrationLogRepository<T> {

    private final String insertMigrationLog;

    private final JdbcTemplate jdbcTemplate;

    public void logError(Exception e, List<? extends T> list, Function<T, String> getMsgIdF) {
        for (T ell : list) {
            updateMigrationInfoError(getMsgIdF.apply(ell), e.getMessage(), new Date(System.currentTimeMillis()), new Date(System.currentTimeMillis()));
        }
    }

    //
    public void logCompleted(List<? extends T> list, Function<T, String> getMsgIdF) {
        for (T ell : list) {
            updateMigrationInfoCompleted(getMsgIdF.apply(ell), new Date(System.currentTimeMillis()), new Date(System.currentTimeMillis()));
        }
    }

    private void updateMigrationInfoCompleted(String msgId, Date start, Date stepExecution) {
        insertMigrationLog(msgId, start, stepExecution, "COMPLETED", "");
    }

    private void updateMigrationInfoError(String msgId, String message, Date start, Date stepExecution) {
        insertMigrationLog(msgId, start, stepExecution, "ERROR", message);
    }

    public void insertMigrationLog(String msgid, Date dateSent, Date dateReceived, String status, String message) {
        jdbcTemplate.update(insertMigrationLog, msgid, dateSent, dateReceived, status, message);
    }
}
