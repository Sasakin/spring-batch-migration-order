package org.charot.migration.order.config;

import com.zaxxer.hikari.HikariDataSource;
import org.charot.migration.order.config.properties.OrderDataSourceProperties;
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
@EnableJpaRepositories(basePackages = "org.charot.migration.order.repository",
        entityManagerFactoryRef = "orderEntityManagerFactory",
        transactionManagerRef = "orderJpaTransactionManager")
public class DatabaseOrderConfig {

    @Bean
    public DataSource orderDataSource(OrderDataSourceProperties properties) {
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
    public LocalContainerEntityManagerFactoryBean orderEntityManagerFactory(DataSource orderDataSource) {
        LocalContainerEntityManagerFactoryBean emf = new LocalContainerEntityManagerFactoryBean();
        emf.setDataSource(orderDataSource);
        emf.setPackagesToScan("org.charot.migration.order.repository.entity");

        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        emf.setJpaVendorAdapter(vendorAdapter);

        return emf;
    }

    @Bean
    public JpaTransactionManager orderJpaTransactionManager(EntityManagerFactory orderEntityManagerFactory) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(orderEntityManagerFactory);
        return transactionManager;
    }

    @Bean
    public TransactionTemplate orderTransactionTemplate(DataSource orderDataSource) {
        return new TransactionTemplate(new DataSourceTransactionManager(orderDataSource));
    }
}
