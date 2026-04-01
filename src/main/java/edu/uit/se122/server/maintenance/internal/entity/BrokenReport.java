package edu.uit.se122.server.maintenance.internal.entity;

import edu.uit.se122.server.common.enums.MaintainStatus;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "BrokenReport")
@Data
public class BrokenReport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer brokenReportId;

    private Boolean isGuest;

    @Enumerated(EnumType.STRING)
    private MaintainStatus status;

    private String content;

    private String attachment;

    private String feedback;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private Integer facilityCategoryId;

    private String userId;
}
