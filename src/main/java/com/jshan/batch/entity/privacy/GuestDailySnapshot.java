package com.jshan.batch.entity.privacy;


import com.jshan.batch.entity.CommonEntity;
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
@Table(name = "bat_rsvp_daily_guest_snapshot", catalog = "emp_batch")
@Entity
public class GuestDailySnapshot extends CommonEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long eventId;
    private Long guestId;
    private String statusCode;
    private String checkinFlag;
    private String confirmFlag;
    private Long organizationId;
    private String organizationName;
    private Long subOrganizationId;
    private String subOrganizationName;
    private Long groupId;
    private String groupName;
    private String date;

    @Builder
    public GuestDailySnapshot(Long eventId, Long guestId, String statusCode, String checkinFlag, String confirmFlag,
                              Long organizationId, String organizationName, Long subOrganizationId, String subOrganizationName,
                              Long groupId, String groupName, String date) {
        this.eventId = eventId;
        this.guestId = guestId;
        this.statusCode = statusCode;
        this.checkinFlag = checkinFlag;
        this.confirmFlag = confirmFlag;
        this.organizationId = organizationId;
        this.organizationName = organizationName;
        this.subOrganizationId = subOrganizationId;
        this.subOrganizationName = subOrganizationName;
        this.groupId = groupId;
        this.groupName = groupName;
        this.date = date;
    }
}
