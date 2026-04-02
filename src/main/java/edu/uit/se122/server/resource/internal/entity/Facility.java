package edu.uit.se122.server.resource.internal.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import edu.uit.se122.server.common.enums.FacilityStatus;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.LocalDateTime;

@Entity
@Table(name = "Facility")
@Data
@ToString(exclude = "category")
@EqualsAndHashCode(exclude = "category")
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
    @JsonBackReference(value = "facilities")
    private FacilityCategory category;
}
