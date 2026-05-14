package edu.uit.se122.server.booking;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface ProductOrderContract {
    record DetailReq(
            Integer productId,
            Integer productCategoryId,
            Integer quantity
    ) {}

    record InvoiceReq(
            Integer courtOrderId,
            BigDecimal givenAmount,
            List<DetailReq> details
    ) {}

    record CalculateInvoiceRes(
            BigDecimal totalAmount,
            BigDecimal changeAmount
    ) {}

    record CreateOrderByCustomer(
            Integer courtOrderId,
            List<DetailReq> details
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
}
