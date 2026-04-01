package edu.uit.se122.server.maintenance.internal.entity;

import edu.uit.se122.server.common.enums.MaintainStatus;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "Maintain")
@Data
public class Maintain {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer maintainId;

    private String detail;

    @Enumerated(EnumType.STRING)
    private MaintainStatus status;

    private LocalDateTime createdAt;

    private LocalDateTime updateAt;

    private Integer categoryId;

    private Integer facilityId;

    private Integer courtId;
}
