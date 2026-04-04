package edu.uit.se122.server.maintenance;

import edu.uit.se122.server.common.enums.MaintainStatus;

import java.time.LocalDateTime;

public interface BrokenReportContract {
    record Request(
        MaintainStatus status,
        String content,
        String attachment
    ) {}

    record Response(
        Integer brokenReportId,
        Boolean guest,
        MaintainStatus status,
        String content,
        String attachment,
        String feedback,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
    ) {}
}
