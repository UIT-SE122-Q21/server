package edu.uit.se122.server.resource;

import java.time.LocalDateTime;

public interface FacilityCriterionContract {
    record Response(
            Integer criterionId,
            String detail,
            LocalDateTime schedule,
            String categoryName
    ) {}

    record Request(
            String detail,
            LocalDateTime schedule,
            Integer categoryId
    ) {}
}
