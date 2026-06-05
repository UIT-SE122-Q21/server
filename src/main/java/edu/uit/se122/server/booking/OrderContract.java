package edu.uit.se122.server.booking;

import com.fasterxml.jackson.annotation.JsonFormat;
import edu.uit.se122.server.common.enums.OrderStatus;
import edu.uit.se122.server.common.enums.PaymentMethod;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public interface OrderContract {
    record CreateOrderByAdminReq(
            LocalDate orderDate,

            @JsonFormat(pattern = "HH:mm:ss")
            @Schema(type = "string", example = "08:00:00")
            LocalTime startHour,

            @JsonFormat(pattern = "HH:mm:ss")
            @Schema(type = "string", example = "10:00:00")
            LocalTime endHour,

            List<Integer> courtIds,
            List<ProductOrderDetailReq> productDetails
    ) {}

    record CreateOrderByCustomerReq(
            LocalDate orderDate,

            @JsonFormat(pattern = "HH:mm:ss")
            @Schema(type = "string", example = "08:00:00")
            LocalTime startHour,

            @JsonFormat(pattern = "HH:mm:ss")
            @Schema(type = "string", example = "10:00:00")
            LocalTime endHour,

            List<Integer> courtIds,
            List<ProductOrderDetailReq> productDetails,
            String guestName,
            String guestEmail,
            String guestPhoneNumber
    ) {}

    record CreateInvoiceByAdminReq(
            BigDecimal givenAmount,
            PaymentMethod paymentMethod
    ) {}

    record ProductOrderDetailReq(
            Integer productId,
            Integer productCategoryId,
            Integer quantity
    ) {}

    record ResByAdmin(
            Integer courtOrderId,
            LocalDate orderDate,
            LocalTime startHour,
            LocalTime endHour,
            OrderStatus status,
            Integer adminId,
            Integer customerId,
            String guestName,
            String guestEmail,
            String guestPhoneNumber,
            LocalDateTime createdAt,
            LocalDateTime updatedAt,
            List<Integer> courtIds
    ) {}

    record ResByCustomer(
            Integer courtOrderId,
            LocalDate orderDate,
            LocalTime startHour,
            LocalTime endHour,
            OrderStatus status,
            List<Integer> courtIds
    ) {}

    record CreatedOrderRes(
            Integer courtOrderId
    ) {}

    record CalculateTotalRes(
            BigDecimal totalBeforeDiscount,
            BigDecimal totalDiscount,
            BigDecimal totalAmount,
            BigDecimal changeAmount,
            String promotionDescription
    ) {}

    record CalculateDepositRes(
            BigDecimal depositAmount
    ) {}

    record ApplyPromotionEvent(
            Integer courtOrderId,
            BigDecimal totalBeforeDiscount
    ) {}

    interface CourtRes {
        Integer getCourtId();
        Integer getCourtOrderId();
        LocalDate getOrderDate();
        LocalTime getStartHour();
        LocalTime getEndHour();
    }

    record CourtReq (
            LocalDate orderDate
    ) {}

    record CreatedOrderEvent(
            Integer courtOrderId,
            LocalDate orderDate,
            LocalTime startHour,
            LocalTime endHour
    ) {}

    record CheckOutEvent(
            Integer courtOrderId
    ) {}
}
