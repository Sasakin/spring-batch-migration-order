package org.charot.migration.common.config;

import lombok.RequiredArgsConstructor;
import org.charot.migration.common.properties.MigrationProp;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import javax.sql.DataSource;

@Configuration
@RequiredArgsConstructor
public class ZTestConfig {

    @Bean
    public JdbcTemplate zTestJdbcTemplate(DataSource zTestDataSource) {
        return new JdbcTemplate(zTestDataSource);
    }

    @Bean
    public PlatformTransactionManager zTestTransactionManager(DataSource zTestDataSource) {
        return new DataSourceTransactionManager(zTestDataSource);
    }

    @Bean
    public TransactionTemplate zTestTransactionTemplate(PlatformTransactionManager zTestTransactionManager) {
        return new TransactionTemplate(zTestTransactionManager);
    }

    @Bean
    public DataSource zTestDataSource(MigrationProp appProp) {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        String jdbcUrl = appProp.getJdbc().getZTest().getFullURL();
        String username = appProp.getJdbc().getZTest().getUser();
        String password = appProp.getJdbc().getZTest().getPass();
        dataSource.setDriverClassName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        dataSource.setUrl(jdbcUrl);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        return dataSource;
    }
}
