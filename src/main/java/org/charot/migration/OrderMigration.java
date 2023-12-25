package org.charot.migration;

import org.charot.migration.common.properties.BatchDataSourceProperties;
import org.charot.migration.common.properties.MigrationProp;
import org.charot.migration.common.properties.SwaggerSecurityProp;
import org.charot.migration.order.config.properties.OrderDataSourceProperties;
import org.charot.migration.storage.config.properties.StorageDataSourceProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;


@SpringBootApplication()
@EnableConfigurationProperties({MigrationProp.class,
        SwaggerSecurityProp.class,
        OrderDataSourceProperties.class,
        StorageDataSourceProperties.class,
        BatchDataSourceProperties.class})
public class OrderMigration {

    public static void main(String[] args) {
        SpringApplication.run(OrderMigration.class, args);
    }
}
