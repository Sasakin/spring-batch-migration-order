package org.charot.migration.storage.repository.mapper;

import org.charot.migration.common.batch.mapper.OrderToEntityMapper;
import org.charot.migration.dao.entity.ZTestAllOrders;
import org.charot.migration.storage.repository.entity.AllOrdersEntity;
import org.mapstruct.Mapper;

@Mapper
public interface AllOrdersEntityMapper extends OrderToEntityMapper<AllOrdersEntity, ZTestAllOrders> {

    AllOrdersEntity reqToEntity(ZTestAllOrders source);
}
