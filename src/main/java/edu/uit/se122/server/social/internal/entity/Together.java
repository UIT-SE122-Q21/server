package edu.uit.se122.server.social.internal.entity;

import edu.uit.se122.server.common.enums.PlanStatus;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "Together")
@Data
public class Together {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer togetherId;

    @Enumerated(EnumType.STRING)
    private PlanStatus status;

    private String content;

    private Integer numOfPlayers;

    private LocalDate planDate;

    private LocalDateTime fromTime;

    private LocalDateTime toTime;

    private String memberId;

    private Integer courtOrderId;
}
