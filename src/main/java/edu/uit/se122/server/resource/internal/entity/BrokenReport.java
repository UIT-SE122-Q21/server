package edu.uit.se122.server.resource.internal.entity;

import edu.uit.se122.server.common.enums.MaintainStatus;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "BrokenReport")
@Getter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class BrokenReport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer brokenReportId;

    @Setter
    @Enumerated(EnumType.STRING)
    private MaintainStatus status;

    @Setter
    private String content;

    @Setter
    private String attachment;

    @Setter
    private String feedback;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "facilityCategoryId")
    private FacilityCategory category;

    @CreatedBy
    @Column(updatable = false)
    private Integer userId;
}
