package org.charot.migration.common.batch;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

@RequiredArgsConstructor
public class MigrationItemWriter<T, K>  implements ItemWriter<T> {

    private final JpaRepository<T, K> repository;

    @Override
    public void write(List<? extends T> items) throws Exception {
        repository.saveAll(items);
    }
}
