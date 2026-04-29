package edu.uit.se122.server.identity;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface ScheduleContract {
    record AdminRes(
            Integer adminId,
            String adminName
    ) {}

    record Res(
            Integer scheduleId,
            LocalDate workDate,
            Integer dayOfWeek,
            LocalTime fromTime,
            LocalTime toTime,
            List<AdminRes> admins
    ) {}

    record Req(
            LocalDate workDate,
            Integer dayOfWeek,

            @JsonFormat(pattern = "HH:mm:ss")
            @Schema(type = "string", example = "08:00:00")
            LocalTime fromTime,

            @JsonFormat(pattern = "HH:mm:ss")
            @Schema(type = "string", example = "10:00:00")
            LocalTime toTime,
            List<Integer> adminIds
    ) {}
}
