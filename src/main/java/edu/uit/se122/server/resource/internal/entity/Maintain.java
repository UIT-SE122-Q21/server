package edu.uit.se122.server.resource.internal.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import edu.uit.se122.server.common.enums.MaintainStatus;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "Maintain")
@Data
@EntityListeners(AuditingEntityListener.class)
@ToString(exclude = {"category", "facility", "court"})
@EqualsAndHashCode(exclude = {"category", "facility", "court"})
public class Maintain {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer maintainId;

    private String detail;

    @Enumerated(EnumType.STRING)
    private MaintainStatus status;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "facilityCategoryId")
    @JsonManagedReference(value = "maintains_category")
    private FacilityCategory category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "facilityId")
    @JsonManagedReference(value = "maintains_facility")
    private Facility facility;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "courtId")
    @JsonManagedReference(value = "maintains_court")
    private Court court;
}
