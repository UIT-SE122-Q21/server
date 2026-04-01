package edu.uit.se122.server.inventory.internal.entity;

import edu.uit.se122.server.common.enums.ProductStatus;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "Product")
@Data
public class Product {
    @Id
    private Integer productId;

    @Column(unique = true, nullable = false)
    private String barcode;

    private String productName;

    private Double unitPrice;

    private Integer quantity;

    @Enumerated(EnumType.STRING)
    private ProductStatus status;

    // Navigation Property: Trỏ ngược về Category
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ProductCategoryId")
    private ProductCategory category;
}
