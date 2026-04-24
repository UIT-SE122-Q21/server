package edu.uit.se122.server.inventory.internal.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import edu.uit.se122.server.common.enums.SaleType;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Entity
@Table(name = "ProductDetail")
@Data
@ToString(exclude = "product")
@EqualsAndHashCode(exclude = "product")
public class ProductDetail {
    @Id
    private Integer productDetailId;

    private String barcode;

    private String unit;

    private Double unitPrice;

    private SaleType saleType;

    private Integer quantity;

    private Integer minQuantity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "productId")
    @JsonManagedReference(value = "details_product")
    private Product product;
}
