package com.jshan.batch.entity.event;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Table(name = "e_event")
@Entity
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    private Long eventGroupId;
    private LocalDateTime guestRegistrationBeginAt;
    private LocalDateTime guestRegistrationEndAt;
    private LocalDateTime eventOperationBeginAt;
    private LocalDateTime eventOperationEndAt;
    private LocalDateTime eventExpirationAt;
    private boolean rsvpFlag;
    private int guestLimitCount;
    private String enrollmentTypeCode;
    private LocalDateTime guestDeletionAt;
    private String host;
    private Boolean activeFlag;

    @Builder
    public Event(String name, String description, Long eventGroupId, LocalDateTime guestRegistrationBeginAt, LocalDateTime guestRegistrationEndAt,
                LocalDateTime eventOperationBeginAt, LocalDateTime eventOperationEndAt, LocalDateTime eventExpirationAt, boolean rsvpFlag,
                int guestLimitCount, String enrollmentTypeCode, LocalDateTime guestDeletionAt, String host, Boolean activeFlag) {
        this.name = name;
        this.description = description;
        this.eventGroupId = eventGroupId;
        this.guestRegistrationBeginAt = guestRegistrationBeginAt;
        this.guestRegistrationEndAt = guestRegistrationEndAt;
        this.eventOperationBeginAt = eventOperationBeginAt;
        this.eventOperationEndAt = eventOperationEndAt;
        this.eventExpirationAt = eventExpirationAt;
        this.rsvpFlag = rsvpFlag;
        this.guestLimitCount = guestLimitCount;
        this.enrollmentTypeCode = enrollmentTypeCode;
        this.guestDeletionAt = guestDeletionAt;
        this.host = host;
        this.activeFlag = activeFlag;
    }
}
