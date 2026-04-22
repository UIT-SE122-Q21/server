package edu.uit.se122.server.resource;

import edu.uit.se122.server.common.enums.MaintainStatus;

import java.time.LocalDateTime;

public interface BrokenReportContract {
    record Request(
        String content,
        String attachment,
        Integer categoryId
    ) {}

    record Response(
        Integer brokenReportId,
        String categoryName,
        MaintainStatus status,
        String content,
        String attachment,
        String feedback,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
    ) {}
}
