package edu.uit.se122.server.inventory.internal.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import edu.uit.se122.server.common.enums.ImportStatus;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "ProductImport")
@Data
@EntityListeners(AuditingEntityListener.class)
public class ProductImport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer importId;

    private String barcode;

    private Integer quantity;

    @Enumerated(EnumType.STRING)
    private ImportStatus status;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updateAt;

    @ManyToOne
    @JoinColumn(name = "ProductId")
    @JsonIgnore
    private Product product;
}
