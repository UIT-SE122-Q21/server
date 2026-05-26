package edu.uit.se122.server.inventory.internal.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import edu.uit.se122.server.common.enums.ProductStatus;
import edu.uit.se122.server.common.enums.SaleType;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Product")
@Getter
@NoArgsConstructor
public class Product {
    @Id
    @Setter
    private Integer productId;

    @Setter
    private String name;

    @Setter
    private String attachment;

    @Setter
    @Enumerated(EnumType.STRING)
    private ProductStatus status;

    @Setter
    private Integer quantity;

    @Setter
    private Integer minQuantity;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ProductCategoryId")
    private ProductCategory category;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    @JsonBackReference(value = "details_product")
    private List<ProductDetail> details = new ArrayList<>();

    public void addDetail(ProductDetail detail) {
        this.details.add(detail);
        detail.setProduct(this);
    }
}
