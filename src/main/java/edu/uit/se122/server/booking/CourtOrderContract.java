package edu.uit.se122.server.booking;

import com.fasterxml.jackson.annotation.JsonFormat;
import edu.uit.se122.server.common.enums.OrderStatus;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface CourtOrderContract {
    record Request(
            LocalDate orderDate,
            OrderStatus status,
            Integer adminId,
            Integer userId,
            List<DetailRequest> detailRequests,
            String guestName,
            String guestEmail,
            String guestPhoneNumber
    ) {}

    record Response(
            Integer courtOrderId,
            OrderStatus status,
            Integer adminId,
            Integer userId,
            Boolean guest,
            String guestName,
            String guestEmail,
            String guestPhoneNumber,
            List<DetailResponse> detailResponses
    ) {}

    record DetailRequest(
            Integer courtId,

            @JsonFormat(pattern = "HH:mm:ss")
            @Schema(type = "string", example = "08:00:00")
            LocalTime fromTime,

            @JsonFormat(pattern = "HH:mm:ss")
            @Schema(type = "string", example = "10:00:00")
            LocalTime toTime
    ) {}

    record DetailResponse(
            Integer courtOrderDetailId,
            Integer courtId,
            LocalTime fromTime,
            LocalTime toTime
    ) {}

    record InvoiceRequest(
            Integer courtOrderId,
            List<DetailResponse> detailResponses
    ) {}
}
