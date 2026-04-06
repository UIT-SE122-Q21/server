package edu.uit.se122.server.resource;

import edu.uit.se122.server.common.enums.FacilityStatus;

import java.time.LocalDateTime;

public interface FacilityContract {
    record Response(
            String facilityId,
            String facilityName,
            String description,
            FacilityStatus status,
            LocalDateTime createdAt,
            LocalDateTime updatedAt,
            String categoryName
    ) {}

    record CreateRequest(
            String facilityName,
            String description,
            Integer categoryId
    ) {}

    record UpdateRequest(
            FacilityStatus status
    ) {}
}
