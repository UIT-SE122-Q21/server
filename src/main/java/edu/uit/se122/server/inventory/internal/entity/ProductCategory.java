package edu.uit.se122.server.inventory.internal.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;

@Entity
@Table(name = "ProductCategory")
@Data
@ToString(exclude = "products")
@EqualsAndHashCode(exclude = "products")
public class ProductCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer productCategoryId;

    private String name;

    private String description;

    private String backgroundColor;

    private String textColor;

    // Navigation Property: Danh sách sản phẩm thuộc loại này
    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
    @JsonManagedReference(value = "products")
    private List<Product> products;
}
