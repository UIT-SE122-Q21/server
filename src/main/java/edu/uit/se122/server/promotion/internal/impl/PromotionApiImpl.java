package edu.uit.se122.server.promotion.internal.impl;

import edu.uit.se122.server.promotion.PromotionApi;
import edu.uit.se122.server.promotion.PromotionContract;
import edu.uit.se122.server.promotion.internal.condition.ConditionComponent;
import edu.uit.se122.server.promotion.internal.engine.PromotionEngine;
import edu.uit.se122.server.promotion.internal.entity.Promotion;
import edu.uit.se122.server.promotion.internal.repository.PromotionRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class PromotionApiImpl implements PromotionApi {
    private final PromotionRepository promotionRepository;
    private final PromotionEngine promotionEngine;

    @Override
    public BigDecimal applyPromotion(PromotionContract.OrderContext dto) {
        BigDecimal totalDiscount = BigDecimal.ZERO;
        List<Promotion> promotions = promotionRepository.findActivePromotions();

        for (Promotion promotion : promotions) {
            ConditionComponent conditionComponent = promotion.getCondition();
            totalDiscount = conditionComponent.calculate(dto, promotionEngine, promotion);
        }
        return totalDiscount;
    }
}
