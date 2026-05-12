package edu.uit.se122.server.inventory.internal.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import edu.uit.se122.server.common.enums.ProductStatus;
import edu.uit.se122.server.common.enums.SaleType;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "ProductDetail")
@Data
@ToString(exclude = "product")
@EqualsAndHashCode(exclude = "product")
public class ProductDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer productDetailId;

    private String barcode;

    private Integer capacity;

    private String unit;

    private BigDecimal unitPrice;

    @Enumerated(EnumType.STRING)
    private SaleType saleType;

    private Integer quantity;

    private Integer minQuantity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "productId")
    @JsonBackReference(value = "details_product")
    private Product product;
}
