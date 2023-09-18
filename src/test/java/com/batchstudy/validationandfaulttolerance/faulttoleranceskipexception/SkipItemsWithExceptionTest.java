package com.batchstudy.validationandfaulttolerance.faulttoleranceskipexception;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.*;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.json.JacksonJsonObjectMarshaller;
import org.springframework.batch.item.json.JacksonJsonObjectReader;
import org.springframework.batch.item.json.JsonFileItemWriter;
import org.springframework.batch.item.json.JsonItemReader;
import org.springframework.batch.item.json.builder.JsonFileItemWriterBuilder;
import org.springframework.batch.item.json.builder.JsonItemReaderBuilder;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;

@SpringBootTest(classes = SkipItemsWithExceptionTest.TestConfig.class)
public class SkipItemsWithExceptionTest {
    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    @Test
    void runJob() throws Exception {
        {
            JobParameters emptyJobParameters = new JobParametersBuilder()
                    .toJobParameters();

            JobExecution jobExecution = jobLauncherTestUtils.launchJob(emptyJobParameters);
        }
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
                    .start(readerStep())
                    .build();
        }

        @Bean
        @JobScope
        public Step readerStep() {
            return stepBuilderFactory.get("readJsonStep")
                    .<SkipTestData, SkipTestData>chunk(1)
                    .reader(reader())
                    .processor((ItemProcessor<SkipTestData, SkipTestData>) item -> {
                        if (item.skipIt) {
                            throw new CustomSkipException();
                        } else {
                            return item;
                        }
                    })
                    .writer(writer())
                    .faultTolerant()
                    .skip(CustomSkipException.class)
                    .skipLimit(1)
                    .build();
        }

        @Bean
        @StepScope
        public JsonItemReader<SkipTestData> reader() {
            File file;

            try {
                file = ResourceUtils.getFile("classpath:files/_E/skipTest.json");
            } catch (FileNotFoundException e) {
                throw new IllegalArgumentException(e);
            }

            return new JsonItemReaderBuilder<SkipTestData>()
                    .jsonObjectReader(new JacksonJsonObjectReader<>(SkipTestData.class))
                    .resource(new FileSystemResource(file))
                    .name("JsonItemReader")
                    .build();
        }

        @Bean
        @StepScope
        public JsonFileItemWriter<SkipTestData> writer() {
            FileSystemResource outputResource = new FileSystemResource("output/skipOutput1.json");
            return new JsonFileItemWriterBuilder<SkipTestData>()
                    .jsonObjectMarshaller(new JacksonJsonObjectMarshaller<>())
                    .resource(outputResource)
                    .name("jsonItemWriter")
                    .build();
        }

        public static class SkipTestData {
            public String item;
            public boolean skipIt;

            @Override
            public String toString() {
                return "SkipTestData{" +
                        "item='" + item + '\'' +
                        ", skipIt='" + skipIt + '\'' +
                        '}';
            }
        }

        @Bean
        public JobLauncherTestUtils utils() {
            return new JobLauncherTestUtils();
        }
    }
}
