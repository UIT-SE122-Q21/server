package edu.uit.se122.server.promotion.internal.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "PromotionDetail")
@Getter
@Setter
public class PromotionDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer promotionDetailId;

    @ManyToOne
    @JoinColumn(name = "PromotionId")
    @JsonBackReference(value = "details")
    private Promotion promotion;

    private Integer productId;
    private Integer minQuantity;
}
