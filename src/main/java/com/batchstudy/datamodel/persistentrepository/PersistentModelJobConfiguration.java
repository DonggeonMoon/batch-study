package com.batchstudy.datamodel.persistentrepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class PersistentModelJobConfiguration {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job persistentModelJob() {
        Step step = stepBuilderFactory.get("step1")
                .tasklet((contribution, chunkContext) -> {
                    Map<String, Object> jobParameters = chunkContext.getStepContext()
                            .getJobParameters();
                    Object outputText = jobParameters.get("outputText");
                    log.info("Your output text is {}",outputText);
                    return RepeatStatus.FINISHED;
                }).build();

        return jobBuilderFactory.get("helloWorldJob")
                .start(step)
                .build();
    }
}