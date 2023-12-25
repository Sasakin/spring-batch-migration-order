package org.charot.migration.common.sql.ztest;

public class StorageSQLQueriesZTest {
    public static final String SELECT_ALL_ORDERS = "SELECT bl.*\n" +
            "FROM file_exchange.all_orders bl\n" +
            "LEFT JOIN foiv.migration_storage_log mb ON bl.id_cftbo = mb.id_cftbo\n" +
            "WHERE mb.id_cftbo IS NULL OR mb.status = 'ERROR'";

    public static final String SELECT_ALL_ORDERS_COUNT = "SELECT count(*)\n" +
            "FROM file_exchange.all_orders bl" ;
    public static final String CREATE_MIGRATION_STORAGE_LOG_TABLE = "IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'foiv.migration_storage_log') AND type in (N'U'))\n" +
            "BEGIN\n" +
            "    CREATE TABLE foiv.migration_storage_log (\n" +
            "        id_cftbo VARCHAR(128) PRIMARY KEY,\n" +
            "        date_sent DATETIME,\n" +
            "        date_received DATETIME,\n" +
            "        status VARCHAR(128),\n" +
            "        message TEXT\n" +
            "    );\n" +
            "\n" +
            "EXEC sys.sp_addextendedproperty \n" +
            "    @name = N'MS_Description', \n" +
            "    @value = N'Таблица migration_storage_log - " +
            "предназначена для проверки статуса миграции таблицы " +
            "order_request в БД сервиса storage',\n" +
            "    @level0type = N'SCHEMA',\n" +
            "    @level0name = N'foiv',\n" +
            "    @level1type = N'TABLE',\n" +
            "    @level1name = N'migration_storage_log';" +
            "END";

    public static final String INSERT_OR_UPDATE_MIGRATION_STORAGE_LOG  = "MERGE INTO foiv.migration_storage_log AS target\n" +
            "USING (VALUES (?, ?, ?, ?, ?)) AS source (id_cftbo, date_sent, date_received, status, message)\n" +
            "ON target.id_cftbo = source.id_cftbo\n" +
            "WHEN MATCHED THEN\n" +
            "    UPDATE SET target.date_sent = source.date_sent,\n" +
            "               target.date_received = source.date_received,\n" +
            "               target.status = source.status,\n" +
            "               target.message = source.message\n" +
            "WHEN NOT MATCHED THEN\n" +
            "    INSERT (id_cftbo, date_sent, date_received, status, message)\n" +
            "    VALUES (source.id_cftbo, source.date_sent, source.date_received, source.status, source.message);";

    public static final String SELECT_MIGRATION_STORAGE_LOG_ERROR = "SELECT * FROM foiv.migration_storage_log WHERE status = 'ERROR'";

    public static final String CLEAR_MIGRATION_STORAGE_LOG = "IF OBJECT_ID('foiv.migration_storage_log', 'U') IS NOT NULL \n" +
            "BEGIN \n" +
            "DELETE FROM foiv.migration_storage_log; \n" +
            "END ";
}
