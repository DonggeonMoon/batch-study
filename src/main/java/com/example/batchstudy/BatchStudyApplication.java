package com.example.batchstudy;

import com.example.batchstudy.service.TriggerJobService;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
@RequiredArgsConstructor
public class BatchStudyApplication {
    public static void main(String[] args) throws JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException, InterruptedException {
        ConfigurableApplicationContext applicationContext = SpringApplication.run(BatchStudyApplication.class, args);
        TriggerJobService triggerJobService = applicationContext.getBean("triggerJobService", TriggerJobService.class);
        triggerJobService.runJob();
    }
}
