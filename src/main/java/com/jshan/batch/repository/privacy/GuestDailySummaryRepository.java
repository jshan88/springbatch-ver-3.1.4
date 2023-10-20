package com.jshan.batch.repository.privacy;

import com.jshan.batch.entity.privacy.GuestDailySummary;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface GuestDailySummaryRepository extends JpaRepository<GuestDailySummary, Long> {

    @Query("select g from GuestDailySummary g " +
        "where (coalesce(:date, g.compositeKey.date) is null or g.compositeKey.date = :date) " +
        "and (coalesce(:eventId, g.compositeKey.eventId) is null or g.compositeKey.eventId = :eventId) " +
        "and (coalesce(:organizationName, g.compositeKey.organizationName) is null or g.compositeKey.organizationName = :organizationName) " +
        "and (coalesce(:subOrganizationName, g.compositeKey.subOrganizationName) is null or g.compositeKey.subOrganizationName = :subOrganizationName) " +
        "and (coalesce(:groupName, g.compositeKey.groupName) is null or g.compositeKey.groupName = :groupName) " +
        "and (coalesce(:statusCode, g.compositeKey.statusCode) is null or g.compositeKey.statusCode = :statusCode)")
    Optional<GuestDailySummary> findByCompositeKey(@Param("date") String date,
                                                   @Param("eventId") Long eventId,
                                                   @Param("organizationName") String organizationName,
                                                   @Param("subOrganizationName") String subOrganizationName,
                                                   @Param("groupName") String groupName,
                                                   @Param("statusCode") String statusCode);
}