package edu.uit.se122.server.inventory.internal.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import edu.uit.se122.server.common.enums.ProductStatus;
import edu.uit.se122.server.common.enums.SaleType;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Product")
@Data
@ToString(exclude = "category")
@EqualsAndHashCode(exclude = "category")
public class Product {
    @Id
    private Integer productId;

    private String name;

    private String attachment;

    @Enumerated(EnumType.STRING)
    private ProductStatus status;

    private Integer quantity;

    private Integer minQuantity;

    // Navigation Property: Trỏ ngược về Category
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ProductCategoryId")
    @JsonBackReference(value = "products")
    private ProductCategory category;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    @JsonBackReference(value = "details_product")
    private List<ProductDetail> details = new ArrayList<>();
}
