package org.charot.migration.common.properties;

import lombok.Getter;
import lombok.Setter;
import org.charot.migration.common.properties.helperClasses.Jdbc;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@Getter
@Setter
@ConfigurationPropertiesScan
@ConfigurationProperties(prefix = "application")
public class MigrationProp {

    private Jdbc jdbc;
}