package edu.uit.se122.server.identity;

import edu.uit.se122.server.common.enums.AdminRole;

public interface AuthContract {
    record RegisterMemberRequest(
            String name,
            String email,
            String password
    ) {}

    record LoginAdminRequest(
            Integer adminId,
            String password
    ) {}

    record LoginMemberRequest(
            String email,
            String password
    ) {}

    record LoginAdminResponse(
            String token,
            Integer adminId,
            String name,
            AdminRole role
    ) {}

    record LoginMemberResponse(
            String accessToken,
            String refreshToken,
            String name,
            String email
    ) {}

    record RefreshTokenRequest(
            String refreshToken
    ) {}

    record CreatedMemberEvent(
            Integer memberId,
            String name
    ) {}
}
