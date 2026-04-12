package edu.uit.se122.server.booking;

import edu.uit.se122.server.common.enums.OrderStatus;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface CourtOrderContract {
    record Request(
            LocalDate orderDate,
            OrderStatus status,
            Integer adminId,
            Integer userId,
            String email,
            List<DetailRequest> detailRequests
    ) {}

    record Response(
            Integer courtOrderId,
            OrderStatus status,
            Integer adminId,
            Integer userId,
            String email,
            Boolean guest,
            List<DetailResponse> detailResponses
    ) {}

    record DetailRequest(
            Integer courtId,
            LocalTime fromTime,
            LocalTime toTime
    ) {}

    record DetailResponse(
            Integer courtOrderDDetailId,
            Integer courtId,
            LocalTime fromTime,
            LocalTime toTime
    ) {}
}
