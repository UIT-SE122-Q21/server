package edu.uit.se122.server.booking.internal.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "ProductCache")
@Data
public class ProductCache {
    @Id
    private Integer productDetailId;
    private String barcode;
    private String name;
    private Double unitPrice;
}
