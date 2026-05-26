package edu.uit.se122.server.resource.internal.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import edu.uit.se122.server.common.enums.FacilityStatus;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "Facility")
@Data
@EntityListeners(AuditingEntityListener.class)
public class Facility {
    @Id
    private Integer facilityId;

    private String name;

    private String description;

    @Enumerated(EnumType.STRING)
    private FacilityStatus status;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updateAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FacilityCategoryId")
    private FacilityCategory category;
}
