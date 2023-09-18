package com.batchstudy.validationandfaulttolerance.defaultvalidation;

import com.batchstudy.listeners.stepexecutionlistener.StepExecutionListenerInOneComponent;
import com.batchstudy.testutils.CourseUtilBatchTestConfig;
import com.batchstudy.utils.CourseUtilJobSummaryListener;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.DefaultJobParametersValidator;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@SpringBootTest(classes = {DefaultValidationTest.TestConfig.class,
        CourseUtilBatchTestConfig.class, StepExecutionListenerInOneComponent.class
})
public class DefaultValidationTest {
    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    @Test
    void test() throws Exception {
        JobParameters jobParameters = new JobParametersBuilder()
                .addParameter("parameterOne", new JobParameter(25L))
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

        @Bean
        public Job job() {
            String[] requiredKey = new String[] {"parameterOne"};
            String[] optionalKey = new String[] {"parameterTwo"};

            DefaultJobParametersValidator defaultValidator =
                    new DefaultJobParametersValidator(requiredKey, optionalKey);
            return jobBuilderFactory.get("myJob")
                    .start(stepOne())
                    .validator(defaultValidator)
                    .listener(new CourseUtilJobSummaryListener())
                    .build();
        }

        @Bean
        @JobScope
        Step stepOne() {
            return stepBuilderFactory.get("myFirstStep")
                    .tasklet((contribution, chunkContext) ->
                            RepeatStatus.FINISHED
                    )
                    .build();
        }

        @Bean
        public JobLauncherTestUtils jobLauncherTestUtils() {
            return new JobLauncherTestUtils();
        }
    }
}
