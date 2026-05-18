package edu.uit.se122.server.booking.internal.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.math.BigDecimal;

@Entity
@Table(name = "ProductCache")
@Data
public class ProductCache {
    @Id
    private Integer productId;
    private Integer productCategoryId;
    private String barcode;
    private Integer capacity;
    private String name;
    private String unit;
    private BigDecimal unitPrice;
}
