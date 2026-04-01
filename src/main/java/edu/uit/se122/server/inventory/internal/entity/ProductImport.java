package edu.uit.se122.server.inventory.internal.entity;

import edu.uit.se122.server.common.enums.ImportStatus;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "ProductImport")
@Data
public class ProductImport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer importId;

    private String barcode;

    private Integer quantity;

    @Enumerated(EnumType.STRING)
    private ImportStatus status;

    private LocalDateTime createdAt;

    private LocalDateTime updateAt;

    @ManyToOne
    @JoinColumn(name = "ProductId")
    private Product product;
}
