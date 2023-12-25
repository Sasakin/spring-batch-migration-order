package org.charot.migration.storage.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@Getter
@Setter
@ConfigurationPropertiesScan
@ConfigurationProperties(prefix = "spring.datasource.storage")
public class StorageDataSourceProperties {

    private String url;

    private String schema;

    private String username;

    private String password;

    @Value("${spring.datasource.storage.hikari.connection-timeout:30000}")
    private int connectionTimeout;

    @Value("${spring.datasource.storage.hikari.minimumIdle:10}")
    private int minimumIdle;

    @Value("${spring.datasource.storage.hikari.maximumPoolSize:250}")
    private int maximumPoolSize;

    @Value("${spring.datasource.storage.hikari.maxLifetime:180000}")
    private int maxLifetime;
}
