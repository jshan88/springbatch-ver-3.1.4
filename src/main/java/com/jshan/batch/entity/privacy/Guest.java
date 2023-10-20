package com.jshan.batch.entity.privacy;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Table(name = "p_guest_identity")
@Entity
public class Guest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long eventId;

    @Enumerated(EnumType.STRING)
    private GuestStatusCode statusCode;

    private String checkinFlag;

    private String confirmFlag;

    private Long groupId;

    private String groupName;

    private Long organizationId;

    private String organizationName;

    private Long subOrganizationId;

    private String subOrganizationName;

    private LocalDateTime createdAt;

    public enum GuestStatusCode {
        NOT_LISTED,
        LISTED,
        EXCLUDED
    }
}
