package org.charot.migration.common.batch.listener;

import lombok.extern.log4j.Log4j2;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;

@Log4j2
public class JobExecutionTimerListener implements JobExecutionListener {

    private long startTime;
    private long endTime;

    @Override
    public void beforeJob(JobExecution jobExecution) {
        startTime = System.currentTimeMillis();
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        endTime = System.currentTimeMillis();
        long executionTime = endTime - startTime;
        long  seconds = executionTime / 1000; // Перевод миллисекунд в секунды
        log.info("Время выполнения job " + jobExecution.getJobInstance().getJobName() + " " + executionTime + " мс");
        log.info("Время выполнения job " + jobExecution.getJobInstance().getJobName() + " " + seconds + " секунд");
    }
}
