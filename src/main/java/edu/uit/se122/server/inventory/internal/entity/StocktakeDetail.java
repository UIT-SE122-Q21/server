package edu.uit.se122.server.inventory.internal.entity;

import edu.uit.se122.server.booking.internal.entity.CourtOrder;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Table(name = "StocktakeDetail")
@Data
public class StocktakeDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer stocktakeDetailId;

    private Integer quantity;

    private LocalDate expireDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Stocktake")
    private Stocktake stocktake;

    private Integer productId;
}
