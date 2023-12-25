package org.charot.migration.common.batch.mapper;

public interface OrderToEntityMapper<T, R> {
    T reqToEntity(R source);
}
