package com.batchstudy.datamodel.jobexecution;

import com.batchstudy.testutils.CourseUtilBatchTestConfig;
import com.batchstudy.utils.CourseUtilJobSummaryListener;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = {JobRepositoryTest.TestConfig.class, CourseUtilBatchTestConfig.class})
public class JobRepositoryTest {
    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    @Autowired
    private JobRepository jobRepository;

    @Test
    void runJob() throws Exception {
        JobParameters jobParameters = new JobParametersBuilder()
                .addParameter("id", new JobParameter(UUID.randomUUID().toString()))
                .toJobParameters();
        jobLauncherTestUtils.launchJob(jobParameters);
        JobExecution jobExecution = jobRepository.getLastJobExecution("myJob", jobParameters);

        assertThat(jobExecution.getStatus()).isEqualTo(BatchStatus.COMPLETED);
    }

    @Configuration
    @EnableBatchProcessing
    static class TestConfig {
        @Autowired
        private JobBuilderFactory jobBuilderFactory;

        @Autowired
        private StepBuilderFactory stepBuilderFactory;

        @Bean
        public Job job() {
            return jobBuilderFactory.get("myJob")
                    .start(stepOne())
                    .next(stepTwo())
                    .listener(new CourseUtilJobSummaryListener())
                    .build();
        }

        @Bean
        @JobScope
        public Step stepOne() {
            return stepBuilderFactory.get("myFirstStep")
                    .tasklet((contribution, chunkContext) ->
                            RepeatStatus.FINISHED)
                    .build();
        }

        @Bean
        @JobScope
        public Step stepTwo() {
            return stepBuilderFactory.get("mySecondStep")
                    .tasklet((contribution, chunkContext) ->
                            RepeatStatus.FINISHED)
                    .build();
        }
    }
}
