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
public class SumComposite implements ConditionComponent {

    private List<ConditionComponent> children = new ArrayList<>();

    @Override
    public BigDecimal calculate(PromotionContract.OrderContext context, PromotionEngine engine, Promotion promotion) {
        // Nếu danh sách rỗng, mặc định là true
        if (children.isEmpty()) return BigDecimal.ZERO;

        // Sử dụng Java Stream để kiểm tra xem TẤT CẢ các con có thỏa mãn không
        return children.stream().map(child -> child.calculate(context, engine, promotion)).reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
