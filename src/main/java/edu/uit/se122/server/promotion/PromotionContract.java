package edu.uit.se122.server.promotion;

import edu.uit.se122.server.common.enums.DiscountType;

import java.time.LocalDate;
import java.util.List;

public interface PromotionContract {
    record Response(
            Integer promotionId,
            String promotionName,
            String description,
            DiscountType discountType,
            Double discountValue,
            LocalDate startDate,
            LocalDate endDate
    ) {}
    
    record Request(
            String promotionName,
            String description,
            DiscountType discountType,
            Double discountValue,
            LocalDate startDate,
            LocalDate endDate,
            List<Integer> productIds
    ) {}
}
