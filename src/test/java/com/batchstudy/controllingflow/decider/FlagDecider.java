package com.batchstudy.controllingflow.decider;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.job.flow.FlowExecutionStatus;
import org.springframework.batch.core.job.flow.JobExecutionDecider;

public class FlagDecider implements JobExecutionDecider {
    @Override
    public FlowExecutionStatus decide(JobExecution jobExecution, StepExecution stepExecution) {
        Object flag = stepExecution.getExecutionContext().get("FLAG");
        return new FlowExecutionStatus(String.valueOf(flag));
    }
}
