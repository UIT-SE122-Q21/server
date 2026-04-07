package edu.uit.se122.server.resource.internal.entity;

import edu.uit.se122.server.common.enums.CourtStatus;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "Court")
@Data
@EntityListeners(AuditingEntityListener.class)
public class Court {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer courtId;

    private String name;

    private Integer numOfIndex;

    private Double unitPrice;

    @Enumerated(EnumType.STRING)
    private CourtStatus status;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;
}
