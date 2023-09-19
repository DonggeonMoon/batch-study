package com.batchstudy.projectone;

import com.batchstudy.projectone.config.AnonymizeJobParameterKeys;
import com.batchstudy.projectone.utils.CourseUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
@RequiredArgsConstructor
@Slf4j
public class Anonymizer {
    private final Job job;
    private final JobLauncher jobLauncher;

    public void runAnonymizationJob() throws Exception {
        jobLauncher.run(job, new JobParameters());
    }

    public void runAnonymizationJob(File uploadedFile) {
        String uploadedFilePath = uploadedFile.getAbsolutePath();

        String completedDirectory = CourseUtils.getWorkDirSubDirectory("public/completed");
        String errorDirectory = CourseUtils.getWorkDirSubDirectory("public/error");
        String processingDirectory = CourseUtils.getWorkDirSubDirectory("private/processing");

        String outputPath = CourseUtils.getFilePathForDifferentDirectory(uploadedFile, completedDirectory);
        String errorPath = CourseUtils.getFilePathForDifferentDirectory(uploadedFile, errorDirectory);
        String processingPath = CourseUtils.getFilePathForDifferentDirectory(uploadedFile, processingDirectory);

        JobParameters jobParameters = new JobParametersBuilder()
                .addString(AnonymizeJobParameterKeys.INPUT_PATH, processingPath)
                .addString(AnonymizeJobParameterKeys.OUTPUT_PATH, outputPath)
                .addString(AnonymizeJobParameterKeys.ERROR_PATH, errorPath)
                .addString(AnonymizeJobParameterKeys.UPLOAD_PATH, uploadedFilePath)
                .addString(AnonymizeJobParameterKeys.ANONYMIZED_DATA, "true")
                .toJobParameters();

        try {
            jobLauncher.run(job, jobParameters);
        } catch (JobExecutionAlreadyRunningException e) {
            log.error("Job is already running", e);
        } catch (JobRestartException e) {
            log.error("Job cannot be restarted");
        } catch (JobInstanceAlreadyCompleteException e) {
            log.warn("Job is already completed");
        } catch (JobParametersInvalidException e) {
            log.error("Job parameters  invalid", e);
        }
    }
}
