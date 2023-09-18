package com.batchstudy.scheduling;

import com.batchstudy.utils.CourseUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class JobScheduler {
    private static final String CRON_EVERY_2_SECONDS = "*/2 * * * * *";

    @Scheduled( cron = "${job.cron}")
    public void startedScheduledJob() {
        runJob();
    }

    public void runJob() {
        log.info("Run a new job");
    }
}
