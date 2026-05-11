package edu.uit.se122.server.identity;

public interface MemberContract {
    record VerifiedEvent(
            Integer memberId,
            String name
    ) {}
}
