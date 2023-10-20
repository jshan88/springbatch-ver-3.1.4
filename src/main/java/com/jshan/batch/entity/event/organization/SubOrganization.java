package com.jshan.batch.entity.event.organization;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Table(name = "e_event_organization_sub")
@Entity
@NoArgsConstructor
public class SubOrganization {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organizationId", referencedColumnName = "id")
    private Organization organization;

    @Column(name = "name", length = 100)
    private String subOrganizationName;

    @Column(length = 100)
    private String description;

    private int limitCount;

    private Boolean activeFlag;

    @Builder
    public SubOrganization(Organization organization, String subOrganizationName, String description, int limitCount, Boolean activeFlag) {
        this.organization = organization;
        this.subOrganizationName = subOrganizationName;
        this.description = description;
        this.limitCount = limitCount;
        this.activeFlag = activeFlag;
    }
}
