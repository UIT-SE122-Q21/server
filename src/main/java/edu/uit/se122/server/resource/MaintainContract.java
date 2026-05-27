package edu.uit.se122.server.resource;

import edu.uit.se122.server.common.enums.MaintainStatus;

import java.time.LocalDateTime;

public interface MaintainContract {
    record Res(
            Integer maintainId,
            String detail,
            MaintainStatus status,
            LocalDateTime createdAt,
            LocalDateTime updatedAt,
            String categoryName,
            String facilityName,
            Integer courtIndex
    ) {}

    record Req(
            String detail,
            Integer categoryId,
            Integer facilityId,
            Integer courtId
    ) {}
}
