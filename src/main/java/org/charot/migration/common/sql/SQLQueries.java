package org.charot.migration.common.sql;

public class SQLQueries {

    public static final String INSERT_OR_UPDATE_ERROR_LOG = "INSERT INTO migration_error_logs (msg_id, log_date, type, exception_message) VALUES (?, ?, ?, ?)" +
            " ON CONFLICT (msg_id) DO UPDATE SET log_date = ?, type = ?, exception_message = ?;";

    public static final String CREATE_MIGRATION_ERROR_LOGS_TABLE = "CREATE TABLE IF NOT EXISTS migration_error_logs (msg_id varchar(128), log_date  timestamp, type varchar(128), exception_message text, PRIMARY KEY (msg_id))";
}
