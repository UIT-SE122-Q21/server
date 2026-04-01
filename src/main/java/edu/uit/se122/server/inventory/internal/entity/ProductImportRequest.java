package edu.uit.se122.server.inventory.internal.entity;

import edu.uit.se122.server.common.enums.ImportRequestStatus;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "ProductImportRequest")
@Data
public class ProductImportRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer importRequestId;

    @ManyToOne
    @JoinColumn(name = "ProductId")
    private Product product;

    private Integer quantity;

    @Enumerated(EnumType.STRING)
    private ImportRequestStatus status;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
