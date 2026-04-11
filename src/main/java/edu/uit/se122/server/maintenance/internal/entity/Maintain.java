package edu.uit.se122.server.maintenance.internal.entity;

import edu.uit.se122.server.common.enums.MaintainStatus;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "Maintain")
@Data
@EntityListeners(AuditingEntityListener.class)
public class Maintain {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer maintainId;

    private String detail;

    @Enumerated(EnumType.STRING)
    private MaintainStatus status;

    @CreatedBy
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updateAt;

    private Integer categoryId;

    private Integer facilityId;

    private Integer courtId;
}
