package com.batchstudy.basics.helloworld.service;

import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class TriggerJobService {
    private final JobLauncher jobLauncher;
    private final Job job;

    public TriggerJobService(JobLauncher jobLauncher, @Qualifier("myJob") Job otherJob) {
        this.jobLauncher = jobLauncher;
        this.job = otherJob;
    }

    public void runJob() throws JobInstanceAlreadyCompleteException,
            JobExecutionAlreadyRunningException,
            JobParametersInvalidException,
            JobRestartException,
            InterruptedException {
        JobParameters jobParameters = new JobParametersBuilder()
                .addParameter("outputText", new JobParameter("My First spring boot app"))
                .toJobParameters();
        jobLauncher.run(job, jobParameters);

        Thread.sleep(3000);

        JobParameters jobParameters2 = new JobParametersBuilder()
                .addParameter("outputText", new JobParameter("Second run"))
                .toJobParameters();

        jobLauncher.run(job, jobParameters2);
    }
}
