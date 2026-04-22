package edu.uit.se122.server.resource.internal.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import edu.uit.se122.server.common.enums.MaintainStatus;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "BrokenReport")
@Data
@EntityListeners(AuditingEntityListener.class)
@ToString(exclude = {"category"})
@EqualsAndHashCode(exclude = {"category"})
public class BrokenReport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer brokenReportId;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "facilityCategoryId")
    @JsonManagedReference(value = "reports_category")
    private FacilityCategory category;

    @CreatedBy
    @Column(updatable = false)
    private Integer userId;
}
