package edu.uit.se122.server.inventory.internal.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import edu.uit.se122.server.common.enums.ProductStatus;
import edu.uit.se122.server.common.enums.SaleType;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "ProductDetail")
@Getter
public class ProductDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer productDetailId;

    @Setter
    private String barcode;

    @Setter
    private Integer capacity;

    @Setter
    private String unit;

    @Setter
    private BigDecimal unitPrice;

    @Setter
    @Enumerated(EnumType.STRING)
    private SaleType saleType;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "productId")
    @JsonBackReference(value = "details_product")
    private Product product;
}
