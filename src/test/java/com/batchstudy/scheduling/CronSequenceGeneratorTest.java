package com.batchstudy.scheduling;


import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.scheduling.support.CronSequenceGenerator;

import java.time.*;
import java.util.Date;

@Slf4j
public class CronSequenceGeneratorTest {
    private static final String EVERY_10_SECONDS = "*/10 * * * * *";
    private static final String EVERY_2ND_MINUTE = "0 */2 * * * *";
    private static final String EVERY_DAY = "0 0 1 * * *";
    private static final String EVERY_20th_day_4th_MONTH = "0 0 0 */20 */4 *";

    @Test
    void testCreateSequence() {
        CronSequenceGenerator sequenceGenerator = new CronSequenceGenerator(EVERY_10_SECONDS);
        Date next = createDateJanuary2020();

        for (int i = 0; i < 10; i++) {
            next = sequenceGenerator.next(next);
            log.info(String.valueOf(next));
        }
    }

    private Date createDateJanuary2020() {
        LocalDate date = LocalDate.of(2020, Month.JANUARY, 1);
        LocalTime time = LocalTime.of(0, 0);
        LocalDateTime startDateTime = LocalDateTime.of(date, time);
        return Date.from(startDateTime.toInstant(ZoneOffset.UTC));
    }
}
