package edu.uit.se122.server.booking.internal.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "ProductOrderDetail")
@Getter
@NoArgsConstructor
public class ProductOrderDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer productOrderDetailId;

    @Setter
    private Integer quantity;

    @Setter
    private BigDecimal unitDiscount;

    @Setter
    @ManyToOne
    @JoinColumn(name = "CourtOrderId")
    @JsonBackReference(value = "productOrderDetails")
    private CourtOrder courtOrder;

    @Setter
    private Integer productId;
}
