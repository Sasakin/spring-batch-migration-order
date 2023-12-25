package org.charot.migration.common.service;

import lombok.NoArgsConstructor;
import org.charot.migration.common.batch.MigrationContextHolder;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.stereotype.Service;

import java.util.concurrent.Executor;

@Service
@NoArgsConstructor
public class StartMigrationService {

    public String startMigration(String jobName, MigrationContextHolder migrationContextHolder, Executor executor, JobOperator jobOperator) {
        migrationContextHolder.getMigrationStart().set(true);
        executor.execute(() -> {
            try {
                jobOperator.start(jobName,
                        String.valueOf(new JobParametersBuilder().addLong("uniqueness", System.currentTimeMillis())));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        return "Migration started successfully. ";
    }
}
