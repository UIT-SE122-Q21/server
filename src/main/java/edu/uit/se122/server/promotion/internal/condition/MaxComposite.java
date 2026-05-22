package edu.uit.se122.server.promotion.internal.condition;

import edu.uit.se122.server.promotion.PromotionContract;
import edu.uit.se122.server.promotion.internal.engine.PromotionEngine;
import edu.uit.se122.server.promotion.internal.entity.Promotion;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class MaxComposite implements ConditionComponent {

    private List<ConditionComponent> children = new ArrayList<>();

    @Override
    public BigDecimal calculate(PromotionContract.OrderContext context, PromotionEngine engine, Promotion promotion) {
        if (children.isEmpty()) return BigDecimal.ZERO;

        // Chỉ cần MỘT con thỏa mãn là được
        return children.stream().map(child -> child.calculate(context, engine, promotion)).max(BigDecimal::compareTo).orElse(BigDecimal.ZERO);
    }
}
