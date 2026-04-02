package edu.uit.se122.server.booking.internal.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.LocalDateTime;

@Entity
@Table(name = "CourtOrderDetail")
@Data
@ToString(exclude = "courtOrder")
@EqualsAndHashCode(exclude = "courtOrder")
public class CourtOrderDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer courtOrderDetailId;

    private LocalDateTime fromTime;

    private LocalDateTime toTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CourtOrderId")
    @JsonBackReference(value = "courtOrderDetails")
    private CourtOrder courtOrder;

    private Integer courtId;
}
