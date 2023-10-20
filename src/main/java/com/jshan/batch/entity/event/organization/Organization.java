package com.jshan.batch.entity.event.organization;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Table(name = "e_event_organization")
@Entity
@NoArgsConstructor
public class Organization {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long eventId;

    @Column(name = "name", length = 100)
    private String organizationName;

    @Column(length = 100)
    private String description;

    private Boolean activeFlag;

    private Boolean hqFlag;

    @Builder
    public Organization(Long eventId, String organizationName, String description, Boolean activeFlag, Boolean hqFlag) {
        this.eventId = eventId;
        this.organizationName = organizationName;
        this.description = description;
        this.activeFlag = activeFlag;
        this.hqFlag = hqFlag;
    }
}
