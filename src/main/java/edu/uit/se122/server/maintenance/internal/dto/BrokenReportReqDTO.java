package edu.uit.se122.server.maintenance.internal.dto;

import edu.uit.se122.server.common.enums.MaintainStatus;
import lombok.Data;

@Data
public class BrokenReportReqDTO {
    private MaintainStatus status;
    private String content;
    private String attachment;
}
