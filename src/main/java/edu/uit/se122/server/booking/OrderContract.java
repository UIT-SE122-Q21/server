package edu.uit.se122.server.booking;

import com.fasterxml.jackson.annotation.JsonFormat;
import edu.uit.se122.server.common.enums.OrderStatus;
import edu.uit.se122.server.common.enums.PaymentMethod;
import io.swagger.v3.oas.annotations.media.Schema;

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

    record Res(
            Integer courtOrderId,
            LocalDate orderDate,
            LocalTime startHour,
            LocalTime endHour,
            OrderStatus status,
            Integer adminId,
            Integer customerId,
            Boolean guest,
            String guestName,
            String guestEmail,
            String guestPhoneNumber,
            LocalDateTime createdAt,
            LocalDateTime updatedAt,
            List<Integer> courtIds
    ) {}

    record OrderHistoryRes(
            LocalDate orderDate,
            String customerName,
            List<OrderHistoryDetailRes> details
    ) {}

    record OrderHistoryDetailRes(
            Integer productDetailId,
            String productName,
            Integer quantity
    ) {}

    record CreatedOrderRes(
            Integer courtOrderId
    ) {}

    record CalculateTotalRes(
            BigDecimal totalAmount,
            BigDecimal changeAmount
    ) {}

    record CalculateDepositRes(
            BigDecimal depositAmount
    ) {}
}
