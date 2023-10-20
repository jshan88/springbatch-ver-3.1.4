package com.jshan.batch.entity.privacy;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Table(name = "temp_guest_daily_snapshot")
@Entity
public class GuestDailySnapshot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long eventId;
    private Long guestId;
    private String statusCode;
    private String checkinFlag;
    private String confirmFlag;
    private String organizationName;
    private String subOrganizationName;
    private String groupName;
    private String date;

    @Builder
    public GuestDailySnapshot(Long eventId, Long guestId, String statusCode, String checkinFlag, String confirmFlag, String organizationName,
                              String subOrganizationName, String groupName, String date) {
        this.eventId = eventId;
        this.guestId = guestId;
        this.statusCode = statusCode;
        this.checkinFlag = checkinFlag;
        this.confirmFlag = confirmFlag;
        this.organizationName = organizationName;
        this.subOrganizationName = subOrganizationName;
        this.groupName = groupName;
        this.date = date;
    }
}
