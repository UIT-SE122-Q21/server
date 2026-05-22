package edu.uit.se122.server.promotion;

import edu.uit.se122.server.promotion.internal.condition.ConditionComponent;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public interface PromotionContract {
    record ResByOperator(
            Integer promotionId,
            String title,
            String description,
            ConditionComponent condition,
            LocalDate startDate,
            LocalDate endDate,
            Boolean hidden,
            List<DetailRes> details
    ) {}

    record ResByAdmin(
            Integer promotionId,
            String title,
            String description,
            LocalDate startDate,
            LocalDate endDate,
            Boolean hidden,
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
            ConditionComponent condition,
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

    @NoArgsConstructor
    @Data
    class OrderContext {
        public BigDecimal totalBeforeDiscount;
        public List<String> promotionDescriptions = new ArrayList<>();
    }

    record ApplyRes(
            BigDecimal totalDiscount,
            List<String> promotionDescriptions
    ) {}
}
