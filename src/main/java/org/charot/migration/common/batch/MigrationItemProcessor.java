package org.charot.migration.common.batch;

import lombok.RequiredArgsConstructor;
import org.charot.migration.common.batch.mapper.OrderToEntityMapper;
import org.springframework.batch.item.ItemProcessor;

import java.util.function.Consumer;

@RequiredArgsConstructor
public class MigrationItemProcessor<R, T> implements ItemProcessor<R, T> {

    private final OrderToEntityMapper<T, R> mapper;
    private final Consumer<T> function;

    @Override
    public T process(R ZFrontRestrictRequest) throws Exception {
        T be = mapper.reqToEntity(ZFrontRestrictRequest);
        function.accept(be);
        return be;
    }
}
