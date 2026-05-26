package edu.uit.se122.server.resource.internal.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import edu.uit.se122.server.common.enums.CourtStatus;
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
@Table(name = "Court")
@Data
@EntityListeners(AuditingEntityListener.class)
public class Court {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer courtId;

    private String name;

    private String description;

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
