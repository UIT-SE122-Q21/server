package edu.uit.se122.server.promotion.internal.dto;

import edu.uit.se122.server.common.enums.DiscountType;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class PromotionResDTO {
    private Integer promotionId;
    private String promotionName;
    private String description;
    private DiscountType discountType;
    private Double discountValue;
    private LocalDate startDate;
    private LocalDate endDate;
    private List<Integer> productIds; // Danh sách ID sản phẩm áp dụng
}