package com.jshan.batch.jobs.rsvpReport.processor;

import com.jshan.batch.entity.privacy.GuestDailySnapshot;
import com.jshan.batch.jobs.rsvpReport.persistence.InMemorySummaryMap.SummaryKey;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
public class GuestSnapshotProcessor implements ItemProcessor<GuestDailySnapshot, SummaryKey> {

    @Override
    public SummaryKey process(GuestDailySnapshot snapshot) {
        return getSummaryKey(snapshot);
    }

    private SummaryKey getSummaryKey(GuestDailySnapshot snapshot) {
        return SummaryKey.builder()
            .date(snapshot.getDate())
            .eventId(snapshot.getEventId())
            .organizationName(snapshot.getOrganizationName())
            .subOrganizationName(snapshot.getSubOrganizationName())
            .groupName(snapshot.getGroupName())
            .statusCode(snapshot.getStatusCode())
            .build();
    }
}
