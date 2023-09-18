package com.batchstudy.listeners.jobexecutionlistenercomponent;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@JobScope
@Slf4j
public class JobListenerAsComponent implements JobExecutionListener {
    @Value("#{jobParameters['outputText']}")
    private String outputText;

    @Autowired
    private JobResultHolder jobResultHolder;

    @Override
    public void beforeJob(JobExecution jobExecution) {
        log.info("Job {} started: {}", jobExecution.getJobId(), outputText);
        log.info("Result is {}", jobResultHolder.getResult());
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        log.info("Result is {}", jobResultHolder.getResult());
        log.info("Job {} ended with status {}", jobExecution.getJobId(), jobExecution.getStatus());
    }
}
