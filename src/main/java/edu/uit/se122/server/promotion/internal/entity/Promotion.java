package edu.uit.se122.server.promotion.internal.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import edu.uit.se122.server.common.enums.DiscountType;
import edu.uit.se122.server.common.enums.PromotionCondition;
import edu.uit.se122.server.common.enums.PromotionType;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Promotion")
@Data
@ToString(exclude = "details")
@EqualsAndHashCode(exclude = "details")
public class Promotion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer promotionId;

    private String title;
    private String description;

    @Enumerated(EnumType.STRING)
    private PromotionType promotionType;

    @Enumerated(EnumType.STRING)
    private DiscountType discountType;

    @Enumerated(EnumType.STRING)
    private PromotionCondition condition;

    private BigDecimal discountValue;
    private BigDecimal minOrderValue;
    private LocalDate startDate;
    private LocalDate endDate;
    private Boolean hidden;

    @OneToMany(mappedBy = "promotion", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference(value = "details")
    private List<PromotionDetail> details = new ArrayList<>();
}
