package org.charot.migration.storage.config;

import lombok.RequiredArgsConstructor;
import org.charot.migration.common.batch.MigrationAllOrdersItemReader;
import org.charot.migration.common.batch.MigrationContextHolder;
import org.charot.migration.common.batch.MigrationItemProcessor;
import org.charot.migration.common.batch.MigrationItemWriter;
import org.charot.migration.common.batch.listener.JobExecutionTimerListener;
import org.charot.migration.common.batch.listener.MigrationChunkListener;
import org.charot.migration.common.batch.listener.MigrationItemProcessListener;
import org.charot.migration.common.batch.listener.MigrationItemReadListener;
import org.charot.migration.common.batch.listener.MigrationItemWriteListener;
import org.charot.migration.common.repository.MigrationExceptionLogRepository;
import org.charot.migration.common.sql.ztest.StorageSQLQueriesZTest;
import org.charot.migration.dao.entity.ZTestAllOrders;
import org.charot.migration.storage.repository.StorageRepository;
import org.charot.migration.storage.repository.entity.AllOrdersEntity;
import org.charot.migration.storage.repository.mapper.AllOrdersEntityMapper;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.support.TransactionTemplate;

import javax.sql.DataSource;

@Configuration
@RequiredArgsConstructor
public class StorageJobConfig {
    public static final String MIGRATION_STORAGE_JOB_NAME = "migrationStorageJob";
    public static final String LOG_MIGRATION_STORAGE_STEP_NAME = "storageLogMigrationTableStep";

    public static final String MIGRATION_STORAGE_STEP_NAME = "storageMigrationStep";

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    private final AllOrdersEntityMapper mapper;

    @Bean
    public Job migrationStorageJob(
            Step storageLogMigrationTableStep,
            Step storageMigrationStep) {
        return jobBuilderFactory.get(MIGRATION_STORAGE_JOB_NAME)
                .incrementer(new RunIdIncrementer())
                .start(storageLogMigrationTableStep)
                .next(storageMigrationStep)
                .listener(new JobExecutionTimerListener())
                .build();
    }

    @Bean
    public Step storageLogMigrationTableStep(JdbcTemplate zTestJdbcTemplate) {
        return stepBuilderFactory.get(LOG_MIGRATION_STORAGE_STEP_NAME)
                .tasklet((contribution, chunkContext) -> {
                    zTestJdbcTemplate.execute(StorageSQLQueriesZTest.CREATE_MIGRATION_STORAGE_LOG_TABLE);
                    return RepeatStatus.FINISHED;
                })
                .build();
    }

    @Bean
    public Step storageMigrationStep(StorageRepository storageRepository,
                                     MigrationContextHolder storageMigrationContextHolder,
                                     DataSource zTestDataSource,
                                     JdbcTemplate zTestJdbcTemplate,
                                     MigrationExceptionLogRepository storageExceptionLogRepository,
                                     @Value("${application.batch.chunk_size:100}")
                                   Integer chunkSize) {
        return stepBuilderFactory.get(MIGRATION_STORAGE_STEP_NAME)
                .<ZTestAllOrders, AllOrdersEntity>chunk(chunkSize)
                .reader(new MigrationAllOrdersItemReader(storageMigrationContextHolder, zTestDataSource,
                        StorageSQLQueriesZTest.SELECT_ALL_ORDERS))
                .processor(new MigrationItemProcessor<>(mapper, storageEntity -> { }))
                .writer(new MigrationItemWriter<>(storageRepository))
                .listener(new MigrationItemReadListener(storageExceptionLogRepository))
                .listener(new MigrationItemWriteListener<>(storageExceptionLogRepository,
                        zTestJdbcTemplate, a -> String.valueOf(a.getId()), StorageSQLQueriesZTest.INSERT_OR_UPDATE_MIGRATION_STORAGE_LOG))
                .listener(new MigrationItemProcessListener(storageExceptionLogRepository))
                .listener(new MigrationChunkListener())
                .build();
    }

    @Bean
    public MigrationExceptionLogRepository storageExceptionLogRepository(DataSource storageDataSource, TransactionTemplate storageTransactionTemplate) {
        return new MigrationExceptionLogRepository(storageDataSource, storageTransactionTemplate);
    }

    @Bean
    public MigrationContextHolder storageMigrationContextHolder() {
        return new MigrationContextHolder();
    }
}
