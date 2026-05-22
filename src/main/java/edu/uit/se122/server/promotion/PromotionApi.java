package edu.uit.se122.server.promotion;

import java.math.BigDecimal;

public interface PromotionApi {
    BigDecimal applyPromotion(PromotionContract.OrderContext dto);
}
