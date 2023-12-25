package org.charot.migration.common.batch;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicBoolean;

@Component
@RequiredArgsConstructor
@Getter
public class MigrationContextHolder {

    private AtomicBoolean migrationStart = new AtomicBoolean(true);
}
