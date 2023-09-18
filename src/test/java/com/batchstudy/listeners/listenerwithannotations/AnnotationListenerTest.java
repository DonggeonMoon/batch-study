package com.batchstudy.listeners.listenerwithannotations;

import com.batchstudy.testutils.CourseUtilBatchTestConfig;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@SpringBootTest(classes = {AnnotationListenerTest.TestConfig.class,
        CourseUtilBatchTestConfig.class, ListenerWithAnnotations.class, ReaderWithBeforeAndAfterStep.class
})
public class AnnotationListenerTest {
    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    @Test
    void test() throws Exception {
        JobParameters jobParameters = new JobParametersBuilder()
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
        private ReaderWithBeforeAndAfterStep readerWithBeforeAndAfterStep;

        @Autowired
        private ListenerWithAnnotations listenerWithAnnotations;

        @Bean
        public Job annotationListenerTest() {
            Step step = stepBuilderFactory.get("step")
                    .chunk(2)
                    .reader(readerWithBeforeAndAfterStep)
                    .writer(items -> {

                    })
                    .listener(listenerWithAnnotations)
                    .build();
            return jobBuilderFactory.get("job")
                    .start(step)
                    .build();
        }

        @Bean
        public JobLauncherTestUtils jobLauncherTestUtils() {
            return new JobLauncherTestUtils();
        }
    }
}
