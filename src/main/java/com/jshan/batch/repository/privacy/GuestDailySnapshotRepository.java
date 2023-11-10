package com.jshan.batch.repository.privacy;

import com.jshan.batch.entity.privacy.GuestDailySnapshot;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GuestDailySnapshotRepository extends JpaRepository<GuestDailySnapshot, Long> {

    List<GuestDailySnapshot> findAllByDate(String date);
}
