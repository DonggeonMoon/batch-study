package com.batchstudy.listeners.stepexecutionlistener;

import com.batchstudy.testutils.CourseUtilBatchTestConfig;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@SpringBootTest(classes = {StepAndListenerTest.TestConfig.class,
        CourseUtilBatchTestConfig.class, StepExecutionListenerInOneComponent.class
})
public class StepAndListenerTest {
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
    @Slf4j
    static class TestConfig {
        @Autowired
        private JobBuilderFactory jobBuilderFactory;

        @Autowired
        private StepBuilderFactory stepBuilderFactory;

        @Autowired
        private StepExecutionListenerInOneComponent stepExecutionListenerInOneComponent;

        @Bean
        public Job executionListenerJob() {
            return jobBuilderFactory.get("helloWorldJob")
                    .start(stepOne())
                    .next(stepTwo())
                    .build();
        }

        @Bean
        @JobScope
        Step stepOne() {
            return stepBuilderFactory.get("myFirstStep")
                    .tasklet(stepExecutionListenerInOneComponent)
                    .listener(stepExecutionListenerInOneComponent)
                    .build();
        }

        @Bean
        @JobScope
        Step stepTwo() {
            return stepBuilderFactory.get("mySecondStep")
                    .tasklet((stepContribution, chunkContext) -> {
                        ExecutionContext executionContext = stepContribution.getStepExecution().getJobExecution().getExecutionContext();
                        int intermediateResult = executionContext.getInt("intermediateResult");
                        log.info("Intermediate Result is {}", intermediateResult);
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
