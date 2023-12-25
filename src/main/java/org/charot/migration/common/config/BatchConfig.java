package org.charot.migration.common.config;

import com.zaxxer.hikari.HikariDataSource;
import org.charot.migration.common.properties.BatchDataSourceProperties;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.support.JobRegistryBeanPostProcessor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import javax.sql.DataSource;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@EnableBatchProcessing
@Configuration
public class BatchConfig {

    @Value("${thread-pool.size}")
    private int threadPoolSize;

    /**
     * @return DataSource for batch processing
     */
    @Bean
    public DataSource dataSource(BatchDataSourceProperties properties) {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl(properties.getUrl());
        dataSource.setUsername(properties.getUsername());
        dataSource.setPassword(properties.getPassword());
        dataSource.setConnectionTimeout(properties.getConnectionTimeout());
        dataSource.setMinimumIdle(properties.getMinimumIdle());
        dataSource.setMaximumPoolSize(properties.getMaximumPoolSize());
        dataSource.setMaxLifetime(properties.getMaxLifetime());
        dataSource.setSchema(properties.getSchema());
        return dataSource;
    }

    @Bean
    public TransactionTemplate transactionTemplate(DataSource dataSource) {
        return new TransactionTemplate(new DataSourceTransactionManager(dataSource));
    }

    @Bean
    public JobRegistryBeanPostProcessor postProcessor(JobRegistry jobRegistry) {
        var processor = new JobRegistryBeanPostProcessor();
        processor.setJobRegistry(jobRegistry);
        return processor;
    }

    @Bean
    public Executor migrationTaskExecutor() {
        return Executors.newFixedThreadPool(threadPoolSize);
    }
}
