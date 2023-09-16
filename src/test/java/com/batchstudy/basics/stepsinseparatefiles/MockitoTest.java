package com.batchstudy.basics.stepsinseparatefiles;

import com.batchstudy.basics.stepsinseparatefiles.config.BatchConfig;
import com.batchstudy.basics.stepsinseparatefiles.dto.InputData;
import com.batchstudy.basics.stepsinseparatefiles.processor.UpperCaseJsonProcessor;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedList;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = {BatchConfig.class, UpperCaseJsonProcessor.class, MockitoTest.TestConfig.class})
@EnableBatchProcessing
public class MockitoTest {
    @Autowired
    private Job job;

    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    @MockBean
    private ItemReader<InputData> itemReader;

    @Test
    void testJob() throws Exception {
        InputData inputData = new InputData();
        inputData.value = "My test data with mockito and in memory reader";

        Mockito.when(itemReader.read())
                .thenReturn(inputData)
                .thenReturn(null);

        TestConfig.inputData.clear();
        TestConfig.inputData.add(inputData);

        JobParameters jobParameters = new JobParametersBuilder()
                .addParameter("inputPath", new JobParameter("classpath:files/_A/input.json"))
                .addParameter("outputPath", new JobParameter("output/upperCaseOutput.json"))
                .toJobParameters();
        JobExecution jobExecution = jobLauncherTestUtils.launchJob(jobParameters);
        BatchStatus status = jobExecution.getStatus();
        assertThat(status).isEqualTo(BatchStatus.COMPLETED);
    }

    @Configuration
    static class TestConfig {
        static LinkedList<InputData> inputData = new LinkedList<>();

        @Bean
        public JobLauncherTestUtils jobLauncherTestUtils() {
            return new JobLauncherTestUtils();
        }

        @Bean
        public ItemReader<InputData> itemReader() {
            return () -> inputData.pollFirst();
        }
    }
}
