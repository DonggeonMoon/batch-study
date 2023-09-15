package com.batchstudy.basics.chunksandstreams;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.*;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.json.JacksonJsonObjectMarshaller;
import org.springframework.batch.item.json.JacksonJsonObjectReader;
import org.springframework.batch.item.json.JsonFileItemWriter;
import org.springframework.batch.item.json.JsonItemReader;
import org.springframework.batch.item.json.builder.JsonFileItemWriterBuilder;
import org.springframework.batch.item.json.builder.JsonItemReaderBuilder;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;

@SpringBootTest(classes = ChunkTest.TestConfig.class)
public class ChunkTest {
    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    @Test
    void runJob() throws Exception {
        {
            JobParameters emptyJobParameters = new JobParametersBuilder()
                    .addParameter("inputPath", new JobParameter("classpath:files/_A/input.json"))
                    .addParameter("outputPath", new JobParameter("output/myOutput.json"))
                    .toJobParameters();

            JobExecution jobExecution = jobLauncherTestUtils.launchJob(emptyJobParameters);
            Assertions.assertEquals(BatchStatus.COMPLETED, jobExecution.getStatus());
        }
        {
            JobParameters emptyJobParameters = new JobParametersBuilder()
                    .addParameter("inputPath", new JobParameter("classpath:files/_A/input.json"))
                    .addParameter("outputPath", new JobParameter("output/myOutput2.json"))
                    .toJobParameters();

            JobExecution jobExecution = jobLauncherTestUtils.launchJob(emptyJobParameters);
            Assertions.assertEquals(BatchStatus.COMPLETED, jobExecution.getStatus());
        }
    }

    @Configuration
    @EnableBatchProcessing
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
            return stepBuilderFactory.get("readJsonStep")
                    .repository(jobRepository)
                    .<InputData, OutputData>chunk(1)
                    .reader(jsonItemReader(null))
                    .processor(processor())
                    .writer(writer(null))
                    .build();
        }

        private ItemProcessor<InputData, OutputData> processor() {
            return inputData -> {
                OutputData outputData = new OutputData();
                outputData.outputValue = inputData.value.toUpperCase();
                return outputData;
            };
        }

        @Bean
        @StepScope
        public JsonFileItemWriter<OutputData> writer(@Value("#{jobParameters['outputPath']}") String outputPath) {
            FileSystemResource outputResource = new FileSystemResource(outputPath);
            return new JsonFileItemWriterBuilder<OutputData>()
                    .jsonObjectMarshaller(new JacksonJsonObjectMarshaller<>())
                    .resource(outputResource)
                    .name("jsonItemWriter")
                    .build();
        }

        @Bean
        @StepScope
        public JsonItemReader<InputData> jsonItemReader(@Value("#{jobParameters['inputPath']}") String inputPath) {
            File file;

            try {
                file = ResourceUtils.getFile(inputPath);
            } catch (FileNotFoundException e) {
                throw new IllegalArgumentException(e);
            }

            return new JsonItemReaderBuilder<InputData>()
                    .jsonObjectReader(new JacksonJsonObjectReader<>(InputData.class))
                    .resource(new FileSystemResource(file))
                    .name("JsonItemReader")
                    .build();
        }

        public static class InputAndOutputData {
            public String value;

            @Override
            public String toString() {
                return "InputAndOutputData{" +
                        "value='" + value + '\'' +
                        '}';
            }
        }
        public static class InputData {
            public String value;

            @Override
            public String toString() {
                return "InputData{" +
                        "value='" + value + '\'' +
                        '}';
            }
        }

        public static class OutputData {
            public String outputValue;

            @Override
            public String toString() {
                return "OutputData{" +
                        "outputValue='" + outputValue + '\'' +
                        '}';
            }
        }

        @Bean
        public JobLauncherTestUtils utils() {
            return new JobLauncherTestUtils();
        }
    }
}
