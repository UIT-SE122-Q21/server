package edu.uit.se122.server.promotion.internal.condition;

import edu.uit.se122.server.promotion.PromotionContract;
import edu.uit.se122.server.promotion.internal.engine.PromotionEngine;
import edu.uit.se122.server.promotion.internal.entity.Promotion;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Slf4j
public class LeafNode implements ConditionComponent {

    private String description; // Ví dụ: "Đơn hàng > 1.000.000đ"
    private String script;      // Ví dụ: "context.orderValue > 1000000"

    @Override
    public BigDecimal calculate(PromotionContract.OrderContext context, PromotionEngine engine, Promotion promotion) {
        return engine.executeScript(this.script, context, promotion);
    }
}
