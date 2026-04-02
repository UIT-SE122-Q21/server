package edu.uit.se122.server.maintenance.internal.dto;

import edu.uit.se122.server.common.enums.MaintainStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BrokenReportResDTO {
    private Integer brokenReportId;
    private Boolean guest;
    private MaintainStatus status;
    private String content;
    private String attachment;
    private String feedback;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
