package edu.uit.se122.server.booking;

import com.fasterxml.jackson.annotation.JsonFormat;
import edu.uit.se122.server.common.enums.OrderStatus;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public interface CourtOrderContract {
    record CreateByAdminReq(
            LocalDate orderDate,

            @JsonFormat(pattern = "HH:mm:ss")
            @Schema(type = "string", example = "08:00:00")
            LocalTime startHour,

            @JsonFormat(pattern = "HH:mm:ss")
            @Schema(type = "string", example = "10:00:00")
            LocalTime endHour,

            List<Integer> courtIds
    ) {}

    record CreateByCustomerReq(
            LocalDate orderDate,

            @JsonFormat(pattern = "HH:mm:ss")
            @Schema(type = "string", example = "08:00:00")
            LocalTime startHour,

            @JsonFormat(pattern = "HH:mm:ss")
            @Schema(type = "string", example = "10:00:00")
            LocalTime endHour,

            List<Integer> courtIds,
            String guestName,
            String guestEmail,
            String guestPhoneNumber
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

    record InvoiceRequest(
            Integer courtOrderId,
            List<Integer> courtIds
    ) {}
}
