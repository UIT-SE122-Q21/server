package edu.uit.se122.server.resource.internal.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import edu.uit.se122.server.common.enums.MaintainStatus;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "Maintain")
@Getter
@NoArgsConstructor
public class Maintain {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer maintainId;

    @Setter
    private String detail;

    @Setter
    @Enumerated(EnumType.STRING)
    private MaintainStatus status;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "facilityCategoryId")
    private FacilityCategory category;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "facilityId")
    private Facility facility;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "courtId")
    private Court court;
}
