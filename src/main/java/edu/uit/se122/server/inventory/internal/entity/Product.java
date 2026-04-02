package edu.uit.se122.server.inventory.internal.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import edu.uit.se122.server.common.enums.ProductStatus;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Entity
@Table(name = "Product")
@Data
@ToString(exclude = "category")
@EqualsAndHashCode(exclude = "category")
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
    @JsonBackReference(value = "products")
    private ProductCategory category;
}
