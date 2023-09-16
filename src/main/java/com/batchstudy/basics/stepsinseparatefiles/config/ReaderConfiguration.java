package com.batchstudy.basics.stepsinseparatefiles.config;

import com.batchstudy.basics.stepsinseparatefiles.dto.InputData;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.json.JacksonJsonObjectReader;
import org.springframework.batch.item.json.builder.JsonItemReaderBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;

@Configuration
@RequiredArgsConstructor
public class ReaderConfiguration {

    @Bean("myJsonItemReader")
    @StepScope
    public ItemReader<InputData> reader(@Value("#{jobParameters['inputPath']}") String inputPath) {
        Resource inputResource = new FileSystemResource(getFileResource(inputPath));

        return new JsonItemReaderBuilder<InputData>()
                .jsonObjectReader(new JacksonJsonObjectReader<>(InputData.class))
                .resource(inputResource)
                .name("jsonItemReader")
                .build();
    }

    private static File getFileResource(String inputPath) {
        try {
            return ResourceUtils.getFile(inputPath);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
