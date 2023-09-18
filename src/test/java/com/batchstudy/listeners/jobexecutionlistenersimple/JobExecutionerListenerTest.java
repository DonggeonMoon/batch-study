package com.batchstudy.listeners.jobexecutionlistenersimple;

import org.junit.jupiter.api.Test;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = JobExecutionerListenerTest.TestConfig.class)
public class JobExecutionerListenerTest {
    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    @Test
    void test() throws Exception {
        JobExecution jobExecution = jobLauncherTestUtils.launchJob(new JobParameters());
        assertThat(jobExecution.getExitStatus().getExitDescription()).isEqualTo("custom description");
    }

    @Configuration
    @EnableBatchProcessing
    static class TestConfig {
        @Autowired
        private JobBuilderFactory jobBuilderFactory;

        @Autowired
        private StepBuilderFactory stepBuilderFactory;

        @Bean
        public Job executionListenerJob() {
            Step step = stepBuilderFactory.get("executionListenerStep")
                    .tasklet((stepContribution, chunkContext) ->
                            RepeatStatus.FINISHED)
                    .build();

            return jobBuilderFactory.get("myJob")

                    .start(step)
                    .listener(new SimpleJobListener())
                    .build();
        }

        @Bean
        public JobLauncherTestUtils jobLauncherTestUtils() {
            return new JobLauncherTestUtils();
        }
    }
}
