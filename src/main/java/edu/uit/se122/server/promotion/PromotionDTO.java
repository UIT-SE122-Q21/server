package edu.uit.se122.server.promotion;

import edu.uit.se122.server.common.enums.DiscountType;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class PromotionDTO {
    private Integer promotionId;
    private String promotionName;
    private String description;
    private DiscountType discountType;
    private Double discountValue;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private List<Integer> productIds; // Danh sách ID sản phẩm áp dụng
}
