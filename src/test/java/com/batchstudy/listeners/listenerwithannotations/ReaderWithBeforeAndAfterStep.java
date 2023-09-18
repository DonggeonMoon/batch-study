package com.batchstudy.listeners.listenerwithannotations;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemReader;
import org.springframework.stereotype.Component;

@Component
@StepScope
@Slf4j
public class ReaderWithBeforeAndAfterStep implements ItemReader<String> {

    private int count = 3;

    @Override
    public String read() {
        count--;
        if (count > 0) {
            return "Value";
        } else {
            return null;
        }
    }

    @BeforeStep
    public void beforeStep(StepExecution stepExecution) {
        count = 15;
    }
}
