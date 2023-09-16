package com.batchstudy.basics.helloworld.config;

import com.batchstudy.basics.helloworld.annotation.MyJob;
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
public class JobConfiguration {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    @MyJob
    public Job helloWorldJob() {
        Step step = stepBuilderFactory.get("step")
                .tasklet((contribution, chunkContext) -> {
                    Map<String, Object> jobParameters = chunkContext.getStepContext().getJobParameters();
                    Object outputText = jobParameters.get("outputText");

                    System.out.println(outputText);
                    return RepeatStatus.FINISHED;
                }).build();
        return jobBuilderFactory.get("helloWorldJob")
                .start(step)
                .build();
    }
}
