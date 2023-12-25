package org.charot.migration.storage.config;

import com.zaxxer.hikari.HikariDataSource;
import org.charot.migration.storage.config.properties.StorageDataSourceProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.support.TransactionTemplate;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

@Configuration
@EnableJpaRepositories(basePackages = "org.charot.migration.storage.repository",
        entityManagerFactoryRef = "storageEntityManagerFactory",
        transactionManagerRef = "storageJpaTransactionManager")
public class DatabaseStorageConfig {

    @Bean
    public DataSource storageDataSource(StorageDataSourceProperties properties) {
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
    public LocalContainerEntityManagerFactoryBean storageEntityManagerFactory(DataSource storageDataSource) {
        LocalContainerEntityManagerFactoryBean emf = new LocalContainerEntityManagerFactoryBean();
        emf.setDataSource(storageDataSource);
        emf.setPackagesToScan("org.charot.migration.storage.repository.entity");

        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        emf.setJpaVendorAdapter(vendorAdapter);

        return emf;
    }

    @Bean
    public JpaTransactionManager storageJpaTransactionManager(EntityManagerFactory storageEntityManagerFactory) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(storageEntityManagerFactory);
        return transactionManager;
    }

    @Bean
    public TransactionTemplate storageTransactionTemplate(DataSource storageDataSource) {
        return new TransactionTemplate(new DataSourceTransactionManager(storageDataSource));
    }
}
