package com.jshan.batch.jobs.rsvpReport.listener;

import com.jshan.batch.entity.privacy.GuestDailySummary;
import com.jshan.batch.jobs.rsvpReport.persistence.InMemorySummaryMap.SummaryKey;
import com.jshan.batch.repository.privacy.GuestDailySummaryRepository;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class SummarizeGuestsStepListener implements StepExecutionListener {

    private final GuestDailySummaryRepository dailySummaryRepository;
    private final Map<SummaryKey, Long> summaryCountMap;

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        if(stepExecution.getExitStatus().equals(ExitStatus.COMPLETED)) {
            List<GuestDailySummary> dailySummaries = filterExistingSummaries();
            dailySummaryRepository.saveAll(dailySummaries);
        }
        summaryCountMap.clear();
        return StepExecutionListener.super.afterStep(stepExecution);
    }

    private List<GuestDailySummary> filterExistingSummaries() {
        return summaryCountMap.entrySet()
                                .stream()
                                .filter(this::shouldUpdateSummary)
                                .map(this::toGuestDailySummary)
                                .toList();
    }

    private boolean shouldUpdateSummary(Entry<SummaryKey, Long> entry) {
        SummaryKey summaryKey = entry.getKey();
        Optional<GuestDailySummary> optionalExistingSummary = dailySummaryRepository.findByCompositeKey(summaryKey.getDate(),
                                                                                                        summaryKey.getEventId(),
                                                                                                        summaryKey.getOrganizationName(),
                                                                                                        summaryKey.getSubOrganizationName(),
                                                                                                        summaryKey.getGroupName(),
                                                                                                        summaryKey.getStatusCode());
        if(optionalExistingSummary.isPresent()) {
            GuestDailySummary existingSummary = optionalExistingSummary.get();
            updateIfCountIsChanged(entry, existingSummary);
            return false;
        }
        return true;
    }

    private void updateIfCountIsChanged(Entry<SummaryKey, Long> entry, GuestDailySummary existingSummary) {
        if(!Objects.equals(existingSummary.getCount(), entry.getValue())) {
            existingSummary.updateCount(entry.getValue());
            dailySummaryRepository.save(existingSummary);
        }
    }

    private GuestDailySummary toGuestDailySummary(Entry<SummaryKey, Long> entry) {
        return GuestDailySummary.builder()
            .compositeKey(entry.getKey().toSummaryCompositeKey())
            .count(entry.getValue())
            .build();
    }
}
