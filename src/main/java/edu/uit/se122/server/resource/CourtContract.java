package edu.uit.se122.server.resource;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface CourtContract {
    record Response(
            Integer courtId,
            Integer numOfIndex,
            Boolean isMaintenance,
            BigDecimal unitPrice,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {}

    record Request(
            Integer numOfIndex
    ) {}

    record UpdateCourtPriceReq(
            BigDecimal newPrice
    ) {}
}
