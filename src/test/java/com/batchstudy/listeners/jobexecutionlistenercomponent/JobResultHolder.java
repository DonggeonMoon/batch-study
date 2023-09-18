package com.batchstudy.listeners.jobexecutionlistenercomponent;

import lombok.Getter;
import lombok.Setter;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.stereotype.Component;

@Component
@JobScope
@Getter
@Setter
public class JobResultHolder {
    private String result;
}
