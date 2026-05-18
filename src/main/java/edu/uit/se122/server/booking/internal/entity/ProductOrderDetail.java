package edu.uit.se122.server.booking.internal.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.math.BigDecimal;

@Entity
@Table(name = "ProductOrderDetail")
@Data
@ToString(exclude = "courtOrder")
@EqualsAndHashCode(exclude = "courtOrder")
public class ProductOrderDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer productOrderDetailId;

    private Integer quantity;

    private BigDecimal unitDiscount;

    @ManyToOne
    @JoinColumn(name = "CourtOrderId")
    @JsonBackReference(value = "productOrderDetails")
    private CourtOrder courtOrder;

    private Integer productId;
}
