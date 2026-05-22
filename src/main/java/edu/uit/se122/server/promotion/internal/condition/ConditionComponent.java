package edu.uit.se122.server.promotion.internal.condition;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import edu.uit.se122.server.promotion.PromotionContract;
import edu.uit.se122.server.promotion.internal.engine.PromotionEngine;
import edu.uit.se122.server.promotion.internal.entity.Promotion;

import java.math.BigDecimal;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = LeafNode.class, name = "LEAF"),
        @JsonSubTypes.Type(value = SumComposite.class, name = "SUM"),
        @JsonSubTypes.Type(value = MaxComposite.class, name = "MAX")
})
public interface ConditionComponent {
    BigDecimal calculate(PromotionContract.OrderContext context, PromotionEngine engine, Promotion promotion);
}
