package edu.uit.se122.server.inventory;

import edu.uit.se122.server.common.enums.ImportStatus;

import java.time.LocalDateTime;

public interface ProductImportContract {
    record Res(
            Integer importId,
            Integer quantity,
            String note,
            ImportStatus status,
            LocalDateTime updatedAt,
            Integer productDetailId,
            String productName
    ) {}

    record CreateReq(
            Integer quantity,
            String note,
            Integer productDetailId
    ) {}
}
