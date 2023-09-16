package com.batchstudy.basics.chunksandstreams;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.support.PassThroughItemProcessor;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.LinkedBlockingDeque;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = StreamTest.TestConfig.class)
@Slf4j
public class StreamTest {
    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;
    private static Deque<String> items = new LinkedBlockingDeque<>(
            List.of("a", "b", "c", "d", "e", "f", "g", "h", "i", "j")
    );

    private static String readNextItem() {
        return items.pollFirst();
    }

    @Test
    void runJob() throws Exception {
        JobParameters emptyJobParameters = new JobParametersBuilder()
                .toJobParameters();

        JobExecution jobExecution = jobLauncherTestUtils.launchJob(emptyJobParameters);
        assertEquals(BatchStatus.COMPLETED, jobExecution.getStatus());
    }

    @Configuration
    @EnableBatchProcessing
    @Slf4j
    static class TestConfig {
        @Autowired
        private JobRepository jobRepository;

        @Autowired
        private JobBuilderFactory jobBuilderFactory;

        @Autowired
        private StepBuilderFactory stepBuilderFactory;

        @Bean
        public Job job(
        ) {
            return jobBuilderFactory.get("myJob")
                    .start(step())
                    .build();
        }

        @Bean
        public Step step() {
            ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
            taskExecutor.setCorePoolSize(4);
            taskExecutor.setMaxPoolSize(4);
            taskExecutor.afterPropertiesSet();
            return stepBuilderFactory.get("jsonItemReader")
                    .repository(jobRepository)
                    .<String, String>chunk(4)
                    .reader(createItemReader())
                    .processor(new PassThroughItemProcessor<>())
                    .writer(createItemWriter())
                    .taskExecutor(taskExecutor)
                    .build();
        }

        private ItemWriter<? super String> createItemWriter() {
            return (ItemWriter<String>) items -> {
                log.info("Write {}", items);
                sleep(200);
            };
        }

        private ItemReader<String> createItemReader() {
            return () -> {
                String item = readNextItem();
                log.info("Read {}", item);
                sleep(1000);
                return item;
            };
        }

        private void sleep(int millis) {
            try {
                Thread.sleep(millis);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        @Bean
        public JobLauncherTestUtils utils() {
            return new JobLauncherTestUtils();
        }
    }
}
