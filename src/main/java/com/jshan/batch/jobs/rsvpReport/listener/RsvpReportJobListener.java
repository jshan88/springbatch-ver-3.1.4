package com.jshan.batch.jobs.rsvpReport.listener;

import com.jshan.batch.repository.privacy.GuestDailySnapshotRepository;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class RsvpReportJobListener implements JobExecutionListener {

    private final GuestDailySnapshotRepository dailySnapshotRepository;

    @Override
    public void beforeJob(JobExecution jobExecution) {
        String jobName = jobExecution.getJobInstance().getJobName();
        LocalDateTime createTime = jobExecution.getStartTime();
        log.info("########## {} Starting at {}", jobName, createTime);
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
//        dailySnapshotRepository.deleteAllInBatch();

        ExitStatus exitStatus = jobExecution.getExitStatus();
        String jobName = jobExecution.getJobInstance().getJobName();
        LocalDateTime endTime = jobExecution.getEndTime();

        log.info("########## {} is done at {}", jobName, endTime);
        log.info("########## Job Exit Status : {}", exitStatus);
    }
}
