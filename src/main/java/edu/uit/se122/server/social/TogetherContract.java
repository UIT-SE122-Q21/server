package edu.uit.se122.server.social;

import edu.uit.se122.server.common.enums.TogetherStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public interface TogetherContract {
    record Res(
            Integer togetherId,
            TogetherStatus status,
            String content,
            Integer numOfPlayersPrefix,
            Integer numOfPlayersJoined,
            LocalDate orderDate,
            LocalTime startHour,
            LocalTime endHour,
            List<Integer> memberIds
    ) {}

    record Req(
            String content,
            Integer numOfPlayersPrefix
    ) {}

    record JoinReq(
            Integer memberId
    ) {}
}
