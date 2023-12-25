package org.charot.migration.common.batch.listener;

import lombok.extern.log4j.Log4j2;
import org.charot.migration.common.repository.MigrationExceptionLogRepository;
import org.charot.migration.dao.repository.ZTestMigrationLogRepository;
import org.springframework.batch.core.ItemWriteListener;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Log4j2
public class MigrationItemWriteListener<T>  implements ItemWriteListener<T> {

    private final ZTestMigrationLogRepository<T> zFrontMigrationLogRepository;

    private final MigrationExceptionLogRepository logRepository;

    private final Function<T, String> getItemIdFunc;

    public MigrationItemWriteListener(MigrationExceptionLogRepository logRepository,
                                      @Qualifier("zTestJdbcTemplate") JdbcTemplate jdbcTemplate,
                                      Function<T, String> getItemIdFunc,
                                      String insertMigrationLogSQL) {
        this.logRepository = logRepository;
        zFrontMigrationLogRepository = new ZTestMigrationLogRepository<>(
                insertMigrationLogSQL,
                jdbcTemplate);
        this.getItemIdFunc = getItemIdFunc;
    }

    @Override
    public void beforeWrite(@NotNull List<? extends T> list) {
        log.info("Начало записи");
    }

    @Override
    public void afterWrite(@NotNull List<? extends T> list) {
        String info = String.join(", ", getItemIdsAsList(list));
        log.info("Конец записи: " + info);
        zFrontMigrationLogRepository.logCompleted(list, getItemIdFunc);
    }

    @Override
    public void onWriteError(@NotNull Exception e, @NotNull List<? extends T> list) {
        List<String> msgIds = getItemIdsAsList(list);
        String info = String.join(", ", msgIds);
        log.error("Ошибка записи для: " + info);

        logRepository.writeReadErrorLogInTransaction(msgIds, "WRITE", e.getMessage());

        zFrontMigrationLogRepository.logError(e, list, getItemIdFunc);
    }

    private List<String> getItemIdsAsList(@NotNull List<? extends T> list) {
        List<String> msgIds = list.stream()
                .map(getItemIdFunc)
                .collect(Collectors.toList());
        return msgIds;
    }
}
