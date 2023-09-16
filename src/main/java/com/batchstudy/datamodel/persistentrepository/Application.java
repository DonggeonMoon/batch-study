package com.batchstudy.datamodel.persistentrepository;

import com.batchstudy.utils.component.trigger.CourseUtilsDefaultTrigger;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;

@EnableAutoConfiguration
@ComponentScan(basePackageClasses = {Application.class, CourseUtilsDefaultTrigger.class})
@EnableBatchProcessing
@EntityScan(basePackageClasses = Application.class)
@PropertySource(value = "/application.yml")
@Slf4j
public class Application {
    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(Application.class, args);
        CourseUtilsDefaultTrigger trigger = context.getBean(CourseUtilsDefaultTrigger.class);
        JobParameters jobParameters = new JobParametersBuilder()
                .addParameter("outputText", new JobParameter("Hello persistent spring batch job." + "2nd job."))
                .toJobParameters();
        try {
            trigger.runJobs(jobParameters);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
}
