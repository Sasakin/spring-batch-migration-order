package org.charot.migration.common.batch.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.batch.core.ChunkListener;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.lang.NonNull;

@Log4j2
@RequiredArgsConstructor
public class MigrationChunkListener implements ChunkListener {

    public void beforeChunk(@NonNull ChunkContext chunkContext) {
        log.info("Начало пачки");
    }

    public void afterChunk(@NonNull ChunkContext chunkContext) {
        log.info("Конец пачки");
    }

    public void afterChunkError(@NonNull ChunkContext chunkContext) {
        log.error("Ошибка пачки");
    }
}
