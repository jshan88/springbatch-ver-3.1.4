package com.jshan.batch.entity.privacy;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Table(name = "bat_rsvp_daily_guest_summary")
@Entity
public class GuestDailySummary {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Embedded
    private SummaryCompositeKey compositeKey;
    private Long count;

    @Builder
    public GuestDailySummary(SummaryCompositeKey compositeKey, Long count) {
        this.compositeKey = compositeKey;
        this.count = count;
    }

    public void updateCount(Long count) {
        this.count = count;
    }

    @Getter
    @NoArgsConstructor
    @Embeddable
    public static class SummaryCompositeKey {
        private String date;
        private Long eventId;
        private String organizationName;
        private String subOrganizationName;
        private String groupName;
        private String statusCode;

        @Builder
        public SummaryCompositeKey(String date, Long eventId, String organizationName, String subOrganizationName, String groupName, String statusCode) {
            this.date = date;
            this.eventId = eventId;
            this.organizationName = organizationName;
            this.subOrganizationName = subOrganizationName;
            this.groupName = groupName;
            this.statusCode = statusCode;
        }
    }
}