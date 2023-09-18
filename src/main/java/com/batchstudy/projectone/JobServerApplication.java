package com.batchstudy.projectone;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackageClasses = JobServerApplication.class)
@EnableBatchProcessing
public class JobServerApplication {
    public static void main(String[] args) throws Exception {
        ConfigurableApplicationContext context = SpringApplication.run(JobServerApplication.class);
        Anonymizer anonymizer = context.getBean(Anonymizer.class);
        anonymizer.runAnonymizationJob();

    }
}
