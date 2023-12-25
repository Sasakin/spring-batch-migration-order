package org.charot.migration.order.repository.mapper;

import org.charot.migration.common.batch.mapper.OrderToEntityMapper;
import org.charot.migration.dao.entity.ZTestOrderRequest;
import org.charot.migration.order.repository.entity.OrderEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface OrderEntityMapper extends OrderToEntityMapper<OrderEntity, ZTestOrderRequest> {
    @Mapping(target = "status", ignore = true)
    OrderEntity reqToEntity(ZTestOrderRequest source);
}
