package edu.uit.se122.server.promotion.internal.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import edu.uit.se122.server.promotion.internal.condition.ConditionComponent;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Promotion")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Promotion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer promotionId;

    private String title;
    private String description;

    @JdbcTypeCode(SqlTypes.JSON)
    private ConditionComponent condition;

    private LocalDate startDate;
    private LocalDate endDate;

    @Setter
    private Boolean hidden;

    @OneToMany(mappedBy = "promotion", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference(value = "details")
    private List<PromotionDetail> details = new ArrayList<>();

    public void addDetail(PromotionDetail detail) {
        if (details != null) {
            this.details.add(detail);
            detail.setPromotion(this);
        }
    }
}
