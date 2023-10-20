package com.jshan.batch.repository.event;

import com.jshan.batch.entity.event.group.GuestGroup;
import java.util.List;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GuestGroupRepository extends JpaRepository<GuestGroup, Long> {

//    public List<GuestGroup> findAllByEventIdAndActiveFlagIsTrue(Long eventId);

    public List<GuestGroup> findAllByActiveFlagIsTrueAndEventIdIsIn(Set<Long> eventIds);
}
