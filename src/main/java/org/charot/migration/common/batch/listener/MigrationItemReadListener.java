package org.charot.migration.common.batch.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.charot.migration.common.repository.MigrationExceptionLogRepository;
import org.charot.migration.dao.entity.ZTestOrderRequest;
import org.springframework.lang.NonNull;

@Log4j2
@RequiredArgsConstructor
public class MigrationItemReadListener implements org.springframework.batch.core.ItemReadListener<ZTestOrderRequest> {

    private final MigrationExceptionLogRepository logRepository;

    public void beforeRead() {
    }

    public void afterRead(@NonNull ZTestOrderRequest o) {
    }

    public void onReadError(@NonNull Exception e) {
        log.error("Ошибка чтения: " + e.getMessage());

       logRepository.writeReadErrorLogInTransaction("READ_" + System.currentTimeMillis(), "READ", e.getMessage());
    }
}
