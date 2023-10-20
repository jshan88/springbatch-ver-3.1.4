package com.jshan.batch.repository.event;

import com.jshan.batch.entity.event.organization.SubOrganization;
import java.util.List;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SubOrganizationRepository extends JpaRepository<SubOrganization, Long> {

    @Query("SELECT sub "
        + "FROM SubOrganization sub JOIN FETCH sub.organization "
        + "WHERE sub.organization.eventId in :eventIds")
    List<SubOrganization> findAllByEventIdIsIn(@Param("eventIds") Set<Long> eventIds);
}