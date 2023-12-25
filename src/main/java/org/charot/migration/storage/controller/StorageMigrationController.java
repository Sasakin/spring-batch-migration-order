package org.charot.migration.storage.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.charot.migration.common.batch.MigrationContextHolder;
import org.charot.migration.common.dto.MigrationInfo;
import org.charot.migration.common.service.StartMigrationService;
import org.charot.migration.common.sql.ztest.StorageSQLQueriesZTest;
import org.charot.migration.dao.entity.ZTestMigrationLog;
import org.charot.migration.dao.repository.ZTestRepository;
import org.charot.migration.storage.config.StorageJobConfig;
import org.charot.migration.storage.repository.StorageRepository;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.Executor;

@RestController
@Tag(name = "Storage migration Api", description = "Api для миграции all_orders в сервис storage.")
@RequestMapping("/storage/migration")
@RequiredArgsConstructor
public class StorageMigrationController {
    private final JobOperator jobOperator;
    private final MigrationContextHolder storageMigrationContextHolder;
    private final Executor migrationTaskExecutor;

    private final StorageRepository storageRepository;

    private final ZTestRepository zTestRepository;

    private final StartMigrationService startMigrationService;

    @GetMapping("/startMigration")
    @Operation(summary = "migration", description = "Action for start migration")
    @SecurityRequirement(name = "basicAuth")
    public String startMigration() {
        return startMigrationService.startMigration(StorageJobConfig.MIGRATION_STORAGE_JOB_NAME,
                storageMigrationContextHolder, migrationTaskExecutor, jobOperator);
    }

    @GetMapping("/stopMigration")
    @Operation(summary = "migration", description = "Action for stop migration")
    @SecurityRequirement(name = "basicAuth")
    public String stopMigration() {
        storageMigrationContextHolder.getMigrationStart().set(false);
        return "Run process for stop block migration.";
    }

    @GetMapping("/info")
    @Operation(summary = "info", description = "Migration job info.")
    @SecurityRequirement(name = "basicAuth")
    public MigrationInfo getMigrationinfo() {
        int currentRows = storageRepository.findAll().size();
        int totalRows = zTestRepository.getCountRowsBySQLFromTest(StorageSQLQueriesZTest.SELECT_ALL_ORDERS_COUNT);
        List<ZTestMigrationLog> logList = zTestRepository.getAllMigrationLogsBySQL(StorageSQLQueriesZTest.SELECT_MIGRATION_STORAGE_LOG_ERROR);
        return new MigrationInfo(currentRows, totalRows, logList);
    }

    @GetMapping("/clearStorageTable")
    @Operation(summary = "clear", description = "Clear all_blocks table before migration")
    @SecurityRequirement(name = "basicAuth")
    public String clearStorageTable() {
        try {
            storageRepository.deleteAll();
            zTestRepository.clearMigrationLogsBySQL(StorageSQLQueriesZTest.CLEAR_MIGRATION_STORAGE_LOG);
            return "Clear all_blocks table success.";
        } catch (Exception e) {
            return "Failed to clear all_blocks table: " + e.getMessage();
        }
    }
}
