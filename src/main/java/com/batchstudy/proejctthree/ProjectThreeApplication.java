package com.batchstudy.proejctthree;

import com.batchstudy.proejctthree.config.AppConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableAutoConfiguration
@EnableScheduling
@ComponentScan(basePackageClasses = {ProjectThreeApplication.class, AppConfiguration.class})
public class ProjectThreeApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProjectThreeApplication.class, args);
    }

}
