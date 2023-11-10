package com.jshan.batch.jobs.rsvpReport.listener;

import com.jshan.batch.entity.privacy.GuestDailySnapshot;
import com.jshan.batch.repository.privacy.GuestDailySnapshotRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class SnapshotNewGuestsStepListener implements StepExecutionListener {

    private final GuestDailySnapshotRepository guestDailySnapshotRepository;

    @Override
    @Transactional
    public void beforeStep(StepExecution stepExecution) {
        String requestDate = stepExecution.getJobParameters().getString("requestDate");
        deleteExistingSnapshotByRequestDate(requestDate);
        StepExecutionListener.super.beforeStep(stepExecution);
    }

    private void deleteExistingSnapshotByRequestDate(String requestDate) {
        List<GuestDailySnapshot> existingSnapshotsByDate = guestDailySnapshotRepository.findAllByDate(requestDate);
        List<Long> existingSnapshotIds = existingSnapshotsByDate.stream()
                                                                .map(GuestDailySnapshot::getId)
                                                                .toList();
        guestDailySnapshotRepository.deleteAllByIdInBatch(existingSnapshotIds);
    }
}
