package edu.uit.se122.server.inventory.internal.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import edu.uit.se122.server.booking.internal.entity.CourtOrder;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.LocalDate;

@Entity
@Table(name = "StocktakeDetail")
@Data
@ToString(exclude = "stocktake")
@EqualsAndHashCode(exclude = "stocktake")
public class StocktakeDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer stocktakeDetailId;

    private Integer quantity;

    private LocalDate expireDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Stocktake")
    @JsonBackReference(value = "stocktakeDetails")
    private Stocktake stocktake;

    private Integer productId;
}
