package edu.uit.se122.server.promotion.internal.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Entity
@Table(name = "PromotionDetail")
@Data
@ToString(exclude = "promotion")
@EqualsAndHashCode(exclude = "promotion")
public class PromotionDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer promotionDetailId;

    @ManyToOne
    @JoinColumn(name = "PromotionId")
    @JsonBackReference(value = "details")
    private Promotion promotion;

    private Integer productId;
}
