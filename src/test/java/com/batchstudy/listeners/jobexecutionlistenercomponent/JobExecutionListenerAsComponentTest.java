package com.batchstudy.listeners.jobexecutionlistenercomponent;

import org.junit.jupiter.api.Test;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = {JobExecutionListenerAsComponentTest.TestConfig.class,
        JobListenerAsComponent.class, JobResultHolder.class
})
public class JobExecutionListenerAsComponentTest {
    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    @Test
    void test() throws Exception {
        JobParameters jobParameters = new JobParametersBuilder()
                .addParameter("outputText", new JobParameter("Hello Spring Batch"))
                .toJobParameters();
        jobLauncherTestUtils.launchJob(jobParameters);
    }

    @Configuration
    @EnableBatchProcessing
    static class TestConfig {
        @Autowired
        private JobBuilderFactory jobBuilderFactory;

        @Autowired
        private StepBuilderFactory stepBuilderFactory;

        @Autowired
        private JobListenerAsComponent jobListenerAsComponent;

        @Autowired
        private JobResultHolder jobResultHolder;

        @Bean
        public Job executionListenerJob() {
            return jobBuilderFactory.get("helloWorldJob")
                    .start(step())
                    .listener(jobListenerAsComponent)
                    .build();
        }

        @Bean
        @JobScope
        Step step() {
            return stepBuilderFactory.get("annotationListenerTest")
                    .tasklet((contribution, chunkContext) -> {
                        String result = "Tasklet result";
                        jobResultHolder.setResult(result);
                        return RepeatStatus.FINISHED;
                    })
                    .build();
        }

        @Bean
        public JobLauncherTestUtils jobLauncherTestUtils() {
            return new JobLauncherTestUtils();
        }
    }
}
