package edu.uit.se122.server.booking;

import java.math.BigDecimal;
import java.util.List;

public interface ProductOrderContract {
    record DetailReq(
            Integer productDetailId,
            Integer quantity
    ) {}

    record InvoiceReq(
            Integer courtOrderId,
            BigDecimal givenAmount,
            List<DetailReq> detailReqs
    ) {}

    record CalculateInvoiceRes(
            BigDecimal totalAmount,
            BigDecimal changeAmount
    ) {}

    record CreateOrderByCustomer(
            Integer courtOrderId,
            List<DetailReq> detailReqs
    ) {}
}
