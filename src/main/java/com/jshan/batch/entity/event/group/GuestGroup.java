package com.jshan.batch.entity.event.group;

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
@Table(name = "e_event_guest_group")
@Entity
public class GuestGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long eventId;
    private String name;
    private String description;
    private String groupColor;
    private Boolean activeFlag;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Builder
    public GuestGroup(Long eventId, String name, String description, String groupColor, Boolean activeFlag, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.eventId = eventId;
        this.name = name;
        this.description = description;
        this.groupColor = groupColor;
        this.activeFlag = activeFlag;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
