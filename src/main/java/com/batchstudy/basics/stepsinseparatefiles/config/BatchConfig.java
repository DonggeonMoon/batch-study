package com.batchstudy.basics.stepsinseparatefiles.config;

import com.batchstudy.basics.stepsinseparatefiles.dto.InputData;
import com.batchstudy.basics.stepsinseparatefiles.dto.OutputData;
import com.batchstudy.basics.stepsinseparatefiles.processor.UpperCaseJsonProcessor;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.SimpleStepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.json.JacksonJsonObjectMarshaller;
import org.springframework.batch.item.json.JacksonJsonObjectReader;
import org.springframework.batch.item.json.JsonFileItemWriter;
import org.springframework.batch.item.json.JsonItemReader;
import org.springframework.batch.item.json.builder.JsonFileItemWriterBuilder;
import org.springframework.batch.item.json.builder.JsonItemReaderBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;

@Configuration
@RequiredArgsConstructor
public class BatchConfig {
    private final JobBuilderFactory jobBuilderFactory;
    private final JobRepository jobRepository;
    private final StepBuilderFactory stepBuilderFactory;
    private final ItemReader<InputData> myJsonItemReader;
    private final UpperCaseJsonProcessor upperCaseJsonProcessor;

    @Bean
    public Job job() {
        return jobBuilderFactory.get("upperCaseProcessingJob")
                .start(step())
                .build();
    }

    private Step step() {
        SimpleStepBuilder<InputData, OutputData> chunk =
                stepBuilderFactory.get("jsonItemReader")
                        .repository(jobRepository)
                        .chunk(1);
        return chunk.reader(myJsonItemReader)
                .processor(upperCaseJsonProcessor)
                .writer(writer(null))
                .build();
    }

    @Bean
    @StepScope
    public JsonFileItemWriter<OutputData> writer(@Value("#{jobParameters['outputPath']}") String outputPath) {
        Resource outputResource = new FileSystemResource(outputPath);
        return new JsonFileItemWriterBuilder<OutputData>()
                .jsonObjectMarshaller(new JacksonJsonObjectMarshaller<>())
                .resource(outputResource)
                .name("jsonItemWriter")
                .build();
    }
}
