package com.jshan.batch.jobs.rsvpReport.reader;

import com.jshan.batch.entity.privacy.GuestDailySnapshot;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.batch.item.database.JpaCursorItemReader;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class GuestSnapshotReader extends JpaCursorItemReader<GuestDailySnapshot> {

    public GuestSnapshotReader(@Qualifier("privacyEntityManagerFactory") EntityManagerFactory privacyEntityManagerFactory) {
        this.setEntityManagerFactory(privacyEntityManagerFactory);
        this.setQueryString(dailySnapshotQuery());
    }
    private String dailySnapshotQuery() {
        return  """
                SELECT g
                FROM GuestDailySnapshot g
                """;
    }
}
