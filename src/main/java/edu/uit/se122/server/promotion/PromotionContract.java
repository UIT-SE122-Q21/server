package edu.uit.se122.server.promotion;

import edu.uit.se122.server.common.enums.DiscountType;
import edu.uit.se122.server.common.enums.PromotionCondition;
import edu.uit.se122.server.common.enums.PromotionType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface PromotionContract {
    record Res(
            Integer promotionId,
            String title,
            String description,
            DiscountType discountType,
            PromotionType promotionType,
            PromotionCondition condition,
            BigDecimal discountValue,
            BigDecimal minOrderValue,
            LocalDate startDate,
            LocalDate endDate,
            List<DetailRes> details
    ) {}

    record DetailRes(
            Integer promotionDetailId,
            Integer productId,
            Integer minQuantity
    ) {}
    
    record CreateReq(
            String title,
            String description,
            DiscountType discountType,
            PromotionType promotionType,
            PromotionCondition condition,
            BigDecimal discountValue,
            BigDecimal minOrderValue,
            LocalDate startDate,
            LocalDate endDate,
            List<CreateDetailReq> details
    ) {}

    record CreateDetailReq(
            Integer productId,
            Integer minQuantity
    ) {}

    record HideReq(
            Boolean hidden
    ) {}
}
