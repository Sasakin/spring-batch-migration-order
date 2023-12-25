package org.charot.migration.common.batch.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.charot.migration.common.repository.MigrationExceptionLogRepository;
import org.charot.migration.dao.entity.ZTestOrderRequest;
import org.charot.migration.order.repository.entity.OrderEntity;
import org.springframework.batch.core.ItemProcessListener;


@Log4j2
@RequiredArgsConstructor
public class MigrationItemProcessListener  implements ItemProcessListener<OrderEntity, ZTestOrderRequest> {

    private final MigrationExceptionLogRepository logRepository;

    @Override
    public void beforeProcess(OrderEntity orderEntity) {
    }

    @Override
    public void afterProcess(OrderEntity orderEntity, ZTestOrderRequest ZFrontRestrictRequest) {
    }

    @Override
    public void onProcessError(OrderEntity orderEntity, Exception e) {
        log.error("Ошибка обработки: " + orderEntity.getMsgId());
        logRepository.writeReadErrorLogInTransaction(orderEntity.getMsgId(), "PROCESS", e.getMessage());
    }
}
