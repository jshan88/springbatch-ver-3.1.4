package com.jshan.batch.jobs.rsvpReport.reader;

import com.jshan.batch.entity.privacy.Guest;
import jakarta.persistence.EntityManagerFactory;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.database.JpaCursorItemReader;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@StepScope
@Component
public class GuestDailyReader extends JpaCursorItemReader<Guest> {

    @Value("#{jobParameters[requestDate]}")
    private String requestDate;

    @Value("#{jobExecutionContext[rsvpEventIdSet]}")
    private Set<Long> rsvpEventIdSet;

    @BeforeStep
    public void setParametersBeforeStep() throws Exception {
        this.setParameterValues(createParameterValues(rsvpEventIdSet, requestDate));
        this.afterPropertiesSet();
    }

    public GuestDailyReader(@Qualifier("privacyEntityManagerFactory") EntityManagerFactory privacyEntityManagerFactory) {
        this.setEntityManagerFactory(privacyEntityManagerFactory);
        this.setQueryString(getDailyGuestReadQuery());
    }

    private Map<String, Object> createParameterValues(Set<Long> rsvpEventIdSet, String requestDate) {
        LocalDateTime toDate = toLocalDateTime(requestDate);
        Map<String, Object> parameterValues = new HashMap<>();
        parameterValues.put("rsvpEventIdSet", rsvpEventIdSet);
        parameterValues.put("toDate", toDate);
        return parameterValues;
    }

    private LocalDateTime toLocalDateTime(String requestDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        LocalDate ld = LocalDate.parse(requestDate, formatter);
        return LocalDateTime.of(ld, LocalTime.MIDNIGHT);
    }

    private String getDailyGuestReadQuery() {
        return  """
                SELECT g
                FROM Guest g
                WHERE 1=1
                AND g.eventId in :rsvpEventIdSet
                AND g.createdAt < :toDate
                """;
    }
}
