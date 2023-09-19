package com.batchstudy.projectone;

import com.batchstudy.projectone.config.AnonymizeJobParameterKeys;
import com.batchstudy.projectone.utils.CourseUtils;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
public class FileHandlingJobExecutionListenerImpl implements FileHandlingJobExecutionListener {
    @Override
    public void beforeJob(JobExecution jobExecution) {
        JobParameters jobParameters = jobExecution.getJobParameters();
        String uploadFile = jobParameters.getString(AnonymizeJobParameterKeys.UPLOAD_PATH);
        String inputFile = jobParameters.getString((AnonymizeJobParameterKeys.INPUT_PATH));
        CourseUtils.moveFileToDirectory(new File(uploadFile), new File(inputFile).getParent());
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        JobParameters jobParameters = jobExecution.getJobParameters();
        String inputFile = jobParameters.getString(AnonymizeJobParameterKeys.INPUT_PATH);

        if (jobExecution.getStatus().equals(BatchStatus.COMPLETED)) {
            CourseUtils.deleteFile(inputFile);
        } else {
            String errorFile = jobParameters.getString(AnonymizeJobParameterKeys.ERROR_PATH);
            CourseUtils.moveFileToDirectory(new File(inputFile), new File(errorFile).getParent());
        }
    }
}
