package com.batchstudy.projectone.config;

import com.batchstudy.projectone.FileHandlingJobExecutionListener;
import com.batchstudy.projectone.model.Person;
import com.batchstudy.projectone.utils.CourseUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.json.JacksonJsonObjectMarshaller;
import org.springframework.batch.item.json.JacksonJsonObjectReader;
import org.springframework.batch.item.json.JsonFileItemWriter;
import org.springframework.batch.item.json.JsonItemReader;
import org.springframework.batch.item.json.builder.JsonFileItemWriterBuilder;
import org.springframework.batch.item.json.builder.JsonItemReaderBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class JobConfiguration {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job job(FileHandlingJobExecutionListener listener) {
        return jobBuilderFactory.get("anonymizeJob")
                .start(step())
                .listener(listener)
                .validator(new AnonymizeJobParameterValidator())
                .build();
    }

    @Bean
    public Step step() {
        return stepBuilderFactory.get("anonymizeStep")
                .<Person, Person>chunk(1)
                .reader(reader(null))
                .processor(processor(null))
                .writer(writer(null))
                .build();
    }

    @Bean
    @StepScope
    public JsonItemReader<Person> reader(@Value(AnonymizeJobParameterKeys.INPUT_PATH_REFERENCE) String inputPath) {
        FileSystemResource resource = CourseUtils.getFileResource(inputPath);

        return new JsonItemReaderBuilder<Person>()
                .name("JsonItemReader")
                .jsonObjectReader(new JacksonJsonObjectReader<>(Person.class))
                .resource(resource)
                .build();
    }

    @Bean
    @StepScope
    public ItemProcessor<Person, Person> processor(@Value(AnonymizeJobParameterKeys.ANONYMIZED_DATA_REFERENCE)
                                                   String anonymize) {
        return input -> {
            if (!input.isCustomer()) {
                return null;
            }
            Person output = Person.from(input);
            if (anonymize != null && anonymize.equals("true")) {
                output.setEmail("");
                output.setName("John Doe");
            }

            return output;
        };
    }

    @Bean
    @StepScope
    public JsonFileItemWriter<Person> writer(@Value(AnonymizeJobParameterKeys.OUTPUT_PATH_REFERENCE) String outputPath) {
        FileSystemResource resource = CourseUtils.getFileResource(outputPath);

        return new JsonFileItemWriterBuilder<Person>()
                .name("jsonFileItemWriter")
                .jsonObjectMarshaller(new JacksonJsonObjectMarshaller<>())
                .resource(resource)
                .build();
    }
}
