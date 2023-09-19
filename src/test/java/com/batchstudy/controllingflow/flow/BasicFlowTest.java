package com.batchstudy.controllingflow.flow;

import com.batchstudy.testutils.CourseUtilBatchTestConfig;
import com.batchstudy.utils.CourseUtilJobSummaryListener;
import lombok.extern.slf4j.Slf4j;
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

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = {BasicFlowTest.TestConfig.class,
        CourseUtilBatchTestConfig.class
})
public class BasicFlowTest {
    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    @Test
    void test() throws Exception {
        JobParameters jobParameters = new JobParametersBuilder()
                .addParameter("parameterOne", new JobParameter(25L))
                .toJobParameters();
        JobExecution jobExecution = jobLauncherTestUtils.launchJob(jobParameters);
        assertEquals(BatchStatus.COMPLETED, jobExecution.getStatus());
    }

    @Configuration
    @EnableBatchProcessing
    @Slf4j
    static class TestConfig {
        @Autowired
        private JobBuilderFactory jobBuilderFactory;

        @Autowired
        private StepBuilderFactory stepBuilderFactory;

        @Bean
        public Job executionListenerJob() {
            return jobBuilderFactory.get("myJob")
                    .flow(stepOne())
                    .on("COMPLETED")
                    .end()
                    .on("FAILED")
                    .to(fallBackStep())
                    .end()
                    .listener(new CourseUtilJobSummaryListener())
                    .build();
        }

        @Bean
        @JobScope
        Step stepOne() {
            return stepBuilderFactory.get("stepOne")
                    .tasklet((contribution, chunkContext) -> {
                        //throw new RuntimeException("failed");
                        return RepeatStatus.FINISHED;
                    })
                    .build();
        }

        @Bean
        @JobScope
        Step fallBackStep() {
            return stepBuilderFactory.get("fallBackStep")
                    .tasklet((contribution, chunkContext) ->
                            RepeatStatus.FINISHED)
                    .build();
        }

        @Bean
        public JobLauncherTestUtils jobLauncherTestUtils() {
            return new JobLauncherTestUtils();
        }
    }
}
