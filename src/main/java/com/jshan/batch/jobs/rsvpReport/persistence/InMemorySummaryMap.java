package com.jshan.batch.jobs.rsvpReport.persistence;

import com.jshan.batch.entity.privacy.GuestDailySummary.SummaryCompositeKey;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class InMemorySummaryMap {

    @Bean
    public Map<SummaryKey, Long> summaryCountMap() {
        return new ConcurrentHashMap<>();
    }

    @Getter
    @EqualsAndHashCode
    public static class SummaryKey {
        private String date;
        private Long eventId;
        private String organizationName;
        private String subOrganizationName;
        private String groupName;
        private String statusCode;

        @Builder
        public SummaryKey(String date, Long eventId, String organizationName, String subOrganizationName, String groupName, String statusCode) {
            this.date = date;
            this.eventId = eventId;
            this.organizationName = organizationName;
            this.subOrganizationName = subOrganizationName;
            this.groupName = groupName;
            this.statusCode = statusCode;
        }

        public SummaryCompositeKey toSummaryCompositeKey() {
            return SummaryCompositeKey.builder()
                .date(date)
                .eventId(eventId)
                .organizationName(organizationName)
                .subOrganizationName(subOrganizationName)
                .groupName(groupName)
                .statusCode(statusCode)
                .build();
        }
    }
}
