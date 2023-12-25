package org.charot.migration.common.batch;

import org.charot.migration.dao.entity.ZTestOrderRequest;
import org.charot.migration.dao.mapper.OrderRowMapper;
import org.springframework.batch.item.database.JdbcCursorItemReader;

import javax.sql.DataSource;

public class MigrationOrderItemReader extends JdbcCursorItemReader<ZTestOrderRequest> {

    private final MigrationContextHolder migrationContextHolder;

    public MigrationOrderItemReader(MigrationContextHolder migrationContextHolder,
                                    DataSource zTestDataSource,
                                    String selectSQL) {
        super();
        this.migrationContextHolder = migrationContextHolder;
        setDataSource(zTestDataSource);
        setSql(selectSQL);
        setRowMapper(new OrderRowMapper());
    }

    @Override
    public ZTestOrderRequest read() throws Exception {
        // Условие остановки миграции
        if (!migrationContextHolder.getMigrationStart().get()) {
            return null;
        }
        return super.read();
    }
}
