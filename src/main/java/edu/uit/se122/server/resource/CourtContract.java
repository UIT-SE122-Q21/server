package edu.uit.se122.server.resource;

import edu.uit.se122.server.common.enums.CourtStatus;

import java.time.LocalDateTime;

public interface CourtContract {
    record Response(
            Integer courtId,
            String name,
            Integer numOfIndex,
            Double unitPrice,
            CourtStatus status,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {}

    record Request(
            String name,
            Integer numOfIndex,
            Double unitPrice,
            CourtStatus status
    ) {}

    record CreatedEvent(
            Integer courtId,
            String name,
            Double unitPrice
    ) {}
}
