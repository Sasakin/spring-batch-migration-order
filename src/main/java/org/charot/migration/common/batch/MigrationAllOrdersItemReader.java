package org.charot.migration.common.batch;

import org.charot.migration.dao.entity.ZTestAllOrders;
import org.charot.migration.dao.mapper.ZTestAllOrdersRowMapper;
import org.springframework.batch.item.database.JdbcCursorItemReader;

import javax.sql.DataSource;

public class MigrationAllOrdersItemReader extends JdbcCursorItemReader<ZTestAllOrders> {

    private final MigrationContextHolder migrationContextHolder;

    public MigrationAllOrdersItemReader(MigrationContextHolder migrationContextHolder,
                                        DataSource zTestDataSource,
                                        String selectSQL) {
        super();
        this.migrationContextHolder = migrationContextHolder;
        setDataSource(zTestDataSource);
        setSql(selectSQL);
        setRowMapper(new ZTestAllOrdersRowMapper());
    }

    @Override
    public ZTestAllOrders read() throws Exception {
        // Условие остановки миграции
        if (!migrationContextHolder.getMigrationStart().get()) {
            return null;
        }
        return super.read();
    }
}
