package edu.uit.se122.server.booking;

import java.util.List;

public interface ProductOrderContract {
    record DetailRequest(
            Integer productId,
            Integer quantity,
            Double racketRentTime
    ) {}

    record InvoiceRequest(
            Integer courtOrderId,
            Double givenAmount,
            List<DetailRequest> detailRequests
    ) {}
}
