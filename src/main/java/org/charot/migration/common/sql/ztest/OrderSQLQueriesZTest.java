package org.charot.migration.common.sql.ztest;

public class OrderSQLQueriesZTest {
    public static final String SELECT_ORDERS = "SELECT rr.*\n" +
            "FROM foiv.order_request rr\n" +
            "LEFT JOIN foiv.migration_order_log mb ON rr.MSG_ID = mb.msgid\n" +
            "WHERE rr.RestrictionType = 1\n" +
            "AND (mb.msgid IS NULL OR mb.status = 'ERROR')";

    public static final String SELECT_ORDERS_COUNT = "SELECT count(*)\n" +
            "FROM foiv.order_request rr\n" +
            "WHERE rr.RestrictionType = 1";
    public static final String CREATE_MIGRATION_ORDER_LOG_TABLE = "IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'foiv.migration_order_log') AND type in (N'U'))\n" +
            "BEGIN\n" +
            "    CREATE TABLE foiv.migration_order_log (\n" +
            "        msgid VARCHAR(128) PRIMARY KEY,\n" +
            "        date_sent DATETIME,\n" +
            "        date_received DATETIME,\n" +
            "        status VARCHAR(128),\n" +
            "        message TEXT\n" +
            "    );\n" +
            "\n" +
            "EXEC sys.sp_addextendedproperty \n" +
            "    @name = N'MS_Description', \n" +
            "    @value = N'Таблица migration_order_log - " +
            "предназначена для проверки статуса миграции таблицы " +
            "foiv.order_request в БД  orders',\n" +
            "    @level0type = N'SCHEMA',\n" +
            "    @level0name = N'foiv',\n" +
            "    @level1type = N'TABLE',\n" +
            "    @level1name = N'migration_order_log';" +
            "END";

    public static final String INSERT_OR_UPDATE_MIGRATION_ORDER_LOG  = "MERGE INTO foiv.migration_order_log AS target\n" +
            "USING (VALUES (?, ?, ?, ?, ?)) AS source (msgid, date_sent, date_received, status, message)\n" +
            "ON target.msgid = source.msgid\n" +
            "WHEN MATCHED THEN\n" +
            "    UPDATE SET target.date_sent = source.date_sent,\n" +
            "               target.date_received = source.date_received,\n" +
            "               target.status = source.status,\n" +
            "               target.message = source.message\n" +
            "WHEN NOT MATCHED THEN\n" +
            "    INSERT (msgid, date_sent, date_received, status, message)\n" +
            "    VALUES (source.msgid, source.date_sent, source.date_received, source.status, source.message);";

    public static final String SELECT_MIGRATION_ORDER_LOG_ERROR = "SELECT * FROM foiv.migration_order_log WHERE status = 'ERROR'";

    public static final String CLEAR_MIGRATION_ORDER_LOG = "IF OBJECT_ID('foiv.migration_order_log', 'U') IS NOT NULL \n" +
            "BEGIN \n" +
            "DELETE FROM foiv.migration_order_log; \n" +
            "END ";
}
