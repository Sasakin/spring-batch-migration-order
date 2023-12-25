package org.charot.migration.order.config;

import lombok.RequiredArgsConstructor;
import org.charot.migration.common.batch.MigrationContextHolder;
import org.charot.migration.common.batch.MigrationItemProcessor;
import org.charot.migration.common.batch.MigrationItemWriter;
import org.charot.migration.common.batch.MigrationOrderItemReader;
import org.charot.migration.common.batch.listener.JobExecutionTimerListener;
import org.charot.migration.common.batch.listener.MigrationChunkListener;
import org.charot.migration.common.batch.listener.MigrationItemProcessListener;
import org.charot.migration.common.batch.listener.MigrationItemReadListener;
import org.charot.migration.common.batch.listener.MigrationItemWriteListener;
import org.charot.migration.common.repository.MigrationExceptionLogRepository;
import org.charot.migration.common.sql.ztest.OrderSQLQueriesZTest;
import org.charot.migration.dao.entity.ZTestOrderRequest;
import org.charot.migration.order.repository.OrderRepository;
import org.charot.migration.order.repository.entity.OrderEntity;
import org.charot.migration.order.repository.mapper.OrderEntityMapper;
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
public class OrderJobConfig {
    public static final String MIGRATION_ORDER_JOB_NAME = "migrationORDERJob";

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    private final OrderEntityMapper mapper;

    @Bean
    public Job migrationORDERJob(
            Step orderLogMigrationTableStep,
            Step orderMigrationStep) {
        return jobBuilderFactory.get(MIGRATION_ORDER_JOB_NAME)
                .incrementer(new RunIdIncrementer())
                .start(orderLogMigrationTableStep)
                .next(orderMigrationStep)
                .listener(new JobExecutionTimerListener())
                .build();
    }

    @Bean
    public Step orderLogMigrationTableStep(JdbcTemplate zTestJdbcTemplate) {
        return stepBuilderFactory.get("ORDERLogMigrationTableStep")
                .tasklet((contribution, chunkContext) -> {
                    zTestJdbcTemplate.execute(OrderSQLQueriesZTest.CREATE_MIGRATION_ORDER_LOG_TABLE);
                    return RepeatStatus.FINISHED;
                })
                .build();
    }

    @Bean
    public Step orderMigrationStep(OrderRepository orderRepository,
                                   MigrationContextHolder orderMigrationContextHolder,
                                   DataSource zTestDataSource,
                                   JdbcTemplate zTestJdbcTemplate,
                                   MigrationExceptionLogRepository orderExceptionLogRepository,
                                   @Value("${application.batch.chunk_size:100}")
                                   Integer chunkSize) {
        return stepBuilderFactory.get("OrderMigrationStep")
                .<ZTestOrderRequest, OrderEntity>chunk(chunkSize)
                .reader(new MigrationOrderItemReader(orderMigrationContextHolder, zTestDataSource,
                        OrderSQLQueriesZTest.SELECT_ORDERS))
                .processor(new MigrationItemProcessor<>(mapper, orderEntity -> orderEntity.setStatus("MIGRATED")))
                .writer(new MigrationItemWriter<>(orderRepository))
                .listener(new MigrationItemReadListener(orderExceptionLogRepository))
                .listener(new MigrationItemWriteListener<>(orderExceptionLogRepository,
                        zTestJdbcTemplate, OrderEntity::getMsgId, OrderSQLQueriesZTest.INSERT_OR_UPDATE_MIGRATION_ORDER_LOG))
                .listener(new MigrationItemProcessListener(orderExceptionLogRepository))
                .listener(new MigrationChunkListener())
                .build();
    }

    @Bean
    public MigrationExceptionLogRepository orderExceptionLogRepository(DataSource orderDataSource, TransactionTemplate orderTransactionTemplate) {
        return new MigrationExceptionLogRepository(orderDataSource, orderTransactionTemplate);
    }

    @Bean
    public MigrationContextHolder orderMigrationContextHolder() {
        return new MigrationContextHolder();
    }
}
