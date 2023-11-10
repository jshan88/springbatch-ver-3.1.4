package com.jshan.batch.jobs.rsvpReport.processor;

import com.jshan.batch.entity.privacy.Guest;
import com.jshan.batch.entity.privacy.GuestDailySnapshot;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@StepScope
@Component
public class GuestDailyProcessor implements ItemProcessor<Guest, GuestDailySnapshot> {

    private final String requestDate;

    public GuestDailyProcessor(@Value("#{jobParameters[requestDate]}") String requestDate) {
        this.requestDate = requestDate;
    }

    @Override
    public GuestDailySnapshot process(Guest guest) {
        return GuestDailySnapshot.builder()
            .eventId(guest.getEventId())
            .guestId(guest.getId())
            .statusCode(guest.getStatusCode().toString())
            .confirmFlag(guest.getConfirmFlag())
            .checkinFlag(guest.getCheckinFlag())
            .groupId(guest.getGroupId())
            .groupName(guest.getGroupName())
            .organizationId(guest.getOrganizationId())
            .organizationName(guest.getOrganizationName())
            .subOrganizationId(guest.getSubOrganizationId())
            .subOrganizationName(guest.getSubOrganizationName())
            .date(requestDate)
            .build();
    }
}
