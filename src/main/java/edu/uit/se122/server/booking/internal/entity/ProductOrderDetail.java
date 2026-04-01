package edu.uit.se122.server.booking.internal.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "ProductOrderDetail")
@Data
public class ProductOrderDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer productOrderDetailId;

    private Integer quantity;

    private Double racketRentTime;

    private Double unitDiscount;

    private Boolean isOrder;

    @ManyToOne
    @JoinColumn(name = "CourtOrderId")
    private CourtOrder courtOrder;

    private Integer productId;
}
