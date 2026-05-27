package edu.uit.se122.server.booking.internal.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalTime;

@Entity
@Table(name = "CourtOrderDetail")
@Getter
@NoArgsConstructor
public class CourtOrderDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer courtOrderDetailId;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CourtOrderId")
    @JsonBackReference(value = "courtOrderDetails")
    private CourtOrder courtOrder;

    @Setter
    private Integer courtId;

    @Setter
    private BigDecimal priceAtBooking;
}
