package edu.uit.se122.server.resource.internal.entity;

import edu.uit.se122.server.common.enums.CourtStatus;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "Court")
@Data
public class Court {
    @Id
    private Integer courtId;

    private String courtName;

    private Integer numOfIndex;

    private Double unitPrice;

    @Enumerated(EnumType.STRING)
    private CourtStatus status;

    private LocalDateTime createdAt;

    private LocalDateTime updateAt;
}
