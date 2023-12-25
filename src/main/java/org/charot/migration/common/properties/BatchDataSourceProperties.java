package org.charot.migration.common.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@Getter
@Setter
@ConfigurationPropertiesScan
@ConfigurationProperties(prefix = "spring.datasource.migration")
public class BatchDataSourceProperties {

    private String url;

    private String schema;

    private String username;

    private String password;

    @Value("${spring.datasource.block.hikari.connection-timeout:30000}")
    private int connectionTimeout;

    @Value("${spring.datasource.block.hikari.minimumIdle:10}")
    private int minimumIdle;

    @Value("${spring.datasource.block.hikari.maximumPoolSize:250}")
    private int maximumPoolSize;

    @Value("${spring.datasource.block.hikari.maxLifetime:180000}")
    private int maxLifetime;
}
