package com.example.batchstudy.config;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
@EnableBatchProcessing
@RequiredArgsConstructor
public class JobConfiguration2 {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job otherJob() {
        Step step = stepBuilderFactory.get("step")
                .tasklet((contribution, chunkContext) -> {
                    Map<String, Object> jobParameters = chunkContext.getStepContext().getJobParameters();
                    Object outputText = jobParameters.get("outputText");

                    System.out.println("Another job: " + outputText);
                    return RepeatStatus.FINISHED;
                }).build();
        return jobBuilderFactory.get("helloWorldJob")
                .start(step)
                .build();
    }
}
