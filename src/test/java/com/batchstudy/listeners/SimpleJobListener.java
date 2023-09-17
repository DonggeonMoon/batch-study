package com.batchstudy.listeners;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;

@Slf4j
public class SimpleJobListener implements JobExecutionListener {

    @Override
    public void beforeJob(JobExecution jobExecution) {
        log.info("Job {} started", jobExecution.getJobId());
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        log.info("Job {} ended with status {}", jobExecution.getJobId(), jobExecution.getStatus());
    }
}
