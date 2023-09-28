package com.batchstudy.proejctthree.controller;

import com.batchstudy.proejctthree.dto.JobExecutionsDto;
import com.batchstudy.proejctthree.dto.JsonWrapper;
import com.batchstudy.proejctthree.services.TriggerJobService;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobExecutionNotRunningException;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.batch.core.launch.NoSuchJobException;
import org.springframework.batch.core.launch.NoSuchJobExecutionException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping(value = "job-monitoring")
@RequiredArgsConstructor
public class JobMonitoringController {

    private final JobRepository jobRepository;
    private final JobExplorer jobExplorer;
    private final TriggerJobService triggerJobService;
    private final List<Job> jobs;
    private final JobOperator jobOperator;
    private final Job simpleJob;
    private final JobRegistry jobRegistry;

    @GetMapping("/job-definitions")
    public JsonWrapper jobDefinitions() {
        return new JsonWrapper(jobs.size());
    }

    @GetMapping("/job-names")
    public JsonWrapper getJobNames() {
        Collection<String> jobNames = jobRegistry.getJobNames();
        return new JsonWrapper(jobNames);
    }

    @PostMapping("/job/start-new")
    public void runJob(@RequestParam(value = "jobName") String jobName) throws NoSuchJobException {
        Job job = jobRegistry.getJob(jobName);
        triggerJobService.triggerJob(job);
    }

    @GetMapping("/job/{jobName}/instances")
    public JsonWrapper getJobInstances(@PathVariable String jobName) {
        List<JobInstance> jobInstances = jobExplorer.getJobInstances(jobName, 0, 100);
        return new JsonWrapper(jobInstances);
    }

    @GetMapping("/job/instance/{instanceId}")
    public JsonWrapper getJobExecutions(@PathVariable Long instanceId) {
        JobInstance jobInstance = jobExplorer.getJobInstance(instanceId);
        List<JobExecution> jobExecutions = jobExplorer.getJobExecutions(jobInstance);
        return new JsonWrapper(new JobExecutionsDto(jobExecutions));
    }

    @PostMapping("/execution/{executionId}/stop")
    public void stopExecution(@PathVariable Long executionId) throws NoSuchJobExecutionException, JobExecutionNotRunningException {
        jobOperator.stop(executionId);
    }

    @PostMapping("/execution/{executionId}/restart")
    public void startExecution(@PathVariable Long executionId) throws JobParametersInvalidException, JobRestartException, JobInstanceAlreadyCompleteException, NoSuchJobExecutionException, NoSuchJobException {
        jobOperator.restart(executionId);
    }

    @PostMapping("/simple-job/start/{parameter}")
    public void startSimpleJobWithParameter(@PathVariable String parameter) {
        JobParameters jobParameters = new JobParametersBuilder()
                .addString("parameter", parameter)
                .toJobParameters();
        triggerJobService.triggerJob(simpleJob, jobParameters);
    }
}
