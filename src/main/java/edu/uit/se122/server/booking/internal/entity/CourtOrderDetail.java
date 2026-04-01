package edu.uit.se122.server.booking.internal.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "CourtOrderDetail")
@Data
public class CourtOrderDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer courtOrderDetailId;

    private LocalDateTime fromTime;

    private LocalDateTime toTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CourtOrderId")
    private CourtOrder courtOrder;

    private Integer courtId;
}
