package com.jshan.batch.repository.event;

import com.jshan.batch.entity.event.Event;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<Event, Long> {

    public List<Event> findAllByActiveFlagIsTrueAndRsvpFlagIsTrue();
}
