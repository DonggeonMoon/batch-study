package com.batchstudy.basics.stepsinseparatefiles.processor;

import com.batchstudy.basics.stepsinseparatefiles.dto.InputData;
import com.batchstudy.basics.stepsinseparatefiles.dto.OutputData;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@StepScope
public class UpperCaseJsonProcessor implements ItemProcessor<InputData, OutputData> {

    @Value("#{jobParameters['inputPath']}")
    private String inputPath;

    @Override
    public OutputData process(InputData inputData) {
        OutputData outputData = new OutputData();
        outputData.value = inputData.value.toUpperCase();
        return outputData;
    }
}
