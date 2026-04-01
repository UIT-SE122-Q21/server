package edu.uit.se122.server.resource.internal.entity;

import edu.uit.se122.server.common.enums.FacilityStatus;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "Facility")
@Data
public class Facility {
    @Id
    private Integer facilityId;

    private String facilityName;

    @Enumerated(EnumType.STRING)
    private FacilityStatus status;

    private LocalDateTime createdAt;

    private LocalDateTime updateAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FacilityCategoryId")
    private FacilityCategory category;
}
