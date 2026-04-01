package edu.uit.se122.server.promotion.internal.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "PromotionDetail")
@Data
public class PromotionDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer promotionDetailId;

    @ManyToOne
    @JoinColumn(name = "PromotionId")
    private Promotion promotion;

    private Integer productId;
}
