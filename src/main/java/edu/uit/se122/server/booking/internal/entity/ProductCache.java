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
    private Integer productDetailId;
    private String barcode;
    private String name;
    private BigDecimal unitPrice;
}
