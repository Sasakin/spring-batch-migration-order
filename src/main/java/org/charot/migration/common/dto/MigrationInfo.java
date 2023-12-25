package org.charot.migration.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.charot.migration.dao.entity.ZTestMigrationLog;

import java.util.List;

@Data
@AllArgsConstructor
public class MigrationInfo {
    private int migratedCount;
    private int allCount;
    private List<ZTestMigrationLog> ZFrontMigrationLogs;
}
