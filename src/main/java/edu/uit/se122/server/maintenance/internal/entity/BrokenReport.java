package edu.uit.se122.server.maintenance.internal.entity;

import edu.uit.se122.server.common.enums.MaintainStatus;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "BrokenReport")
@Data
@EntityListeners(AuditingEntityListener.class)
public class BrokenReport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer brokenReportId;

    private Boolean guest;

    @Enumerated(EnumType.STRING)
    private MaintainStatus status;

    private String content;

    private String attachment;

    private String feedback;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    private Integer facilityCategoryId;

    @CreatedBy
    @Column(updatable = false)
    private String userId;
}
