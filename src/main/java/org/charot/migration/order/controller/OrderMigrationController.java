package org.charot.migration.order.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.charot.migration.common.batch.MigrationContextHolder;
import org.charot.migration.common.dto.MigrationInfo;
import org.charot.migration.common.service.StartMigrationService;
import org.charot.migration.common.sql.ztest.OrderSQLQueriesZTest;
import org.charot.migration.dao.entity.ZTestMigrationLog;
import org.charot.migration.dao.repository.ZTestRepository;
import org.charot.migration.order.repository.OrderRepository;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.Executor;

import static org.charot.migration.order.config.OrderJobConfig.MIGRATION_ORDER_JOB_NAME;

@RestController
@Tag(name = "order migration Api", description = "Api для миграции заказов.")
@RequestMapping("/order/migration")
@RequiredArgsConstructor
public class OrderMigrationController {

    private final JobOperator jobOperator;
    private final MigrationContextHolder orderMigrationContextHolder;
    private final Executor migrationTaskExecutor;

    private final OrderRepository orderRepository;

    private final ZTestRepository zTestRepository;

    private final StartMigrationService startMigrationService;

    @GetMapping("/startMigration")
    @Operation(summary = "migration", description = "Action for start migration")
    @SecurityRequirement(name = "basicAuth")
    public String startMigration() {
        return startMigrationService.startMigration(MIGRATION_ORDER_JOB_NAME,
                orderMigrationContextHolder, migrationTaskExecutor, jobOperator);
    }

    @GetMapping("/stopMigration")
    @Operation(summary = "migration", description = "Action for stop migration")
    @SecurityRequirement(name = "basicAuth")
    public String stopMigration() {
            orderMigrationContextHolder.getMigrationStart().set(false);
            return "Run process for stop order migration.";
    }

    @GetMapping("/info")
    @Operation(summary = "info", description = "Migration job info.")
    @SecurityRequirement(name = "basicAuth")
    public MigrationInfo getMigrationinfo() {
        int currentRows = orderRepository.findAll().size();
        int totalRows = zTestRepository.getCountRowsBySQLFromTest(OrderSQLQueriesZTest.SELECT_ORDERS_COUNT);
        List<ZTestMigrationLog> logList = zTestRepository.getAllMigrationLogsBySQL(OrderSQLQueriesZTest.SELECT_MIGRATION_ORDER_LOG_ERROR);
        return new MigrationInfo(currentRows, totalRows, logList);
    }

    @GetMapping("/clearOrderTable")
    @Operation(summary = "clear", description = "Clear order table before migration")
    @SecurityRequirement(name = "basicAuth")
    public String clearorderTable() {
        try {
            orderRepository.deleteAll();
            zTestRepository.clearMigrationLogsBySQL(OrderSQLQueriesZTest.CLEAR_MIGRATION_ORDER_LOG);
            return "Clear order table success.";
        } catch (Exception e) {
            return "Failed to clear order table: " + e.getMessage();
        }
    }
}
