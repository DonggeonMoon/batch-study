package com.batchstudy.projectone;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class Anonymizer {
    private final Job job;
    private final JobLauncher jobLauncher;

    public void runAnonymizationJob() throws Exception {
        jobLauncher.run(job, new JobParameters());
    }
}
