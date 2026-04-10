package edu.uit.se122.server.social;

import edu.uit.se122.server.common.enums.TogetherStatus;

import java.time.LocalDateTime;

public interface TogetherContract {
    record Response(
            Integer togetherId,
            TogetherStatus status,
            String content,
            Integer numOfPlayers,
            LocalDateTime fromTime,
            LocalDateTime toTime
    ) {}

    record Request(
            TogetherStatus status,
            String content,
            Integer numOfPlayers,
            LocalDateTime fromTime,
            LocalDateTime toTime
    ) {}
}
