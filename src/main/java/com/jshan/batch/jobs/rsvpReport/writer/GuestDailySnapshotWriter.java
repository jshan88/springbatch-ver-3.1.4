package com.jshan.batch.jobs.rsvpReport.writer;

import com.jshan.batch.entity.privacy.GuestDailySnapshot;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class GuestDailySnapshotWriter extends JpaItemWriter<GuestDailySnapshot> {

    public GuestDailySnapshotWriter(@Qualifier("privacyEntityManagerFactory") EntityManagerFactory privacyEntityManagerFactory) {
        super.setEntityManagerFactory(privacyEntityManagerFactory);
    }
}