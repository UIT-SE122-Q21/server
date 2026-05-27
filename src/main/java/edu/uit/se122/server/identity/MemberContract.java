package edu.uit.se122.server.identity;

import java.time.LocalDateTime;

public interface MemberContract {
    record Res(
            Integer memberId,
            String name,
            String email,
            Boolean isVerified,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {}
}
