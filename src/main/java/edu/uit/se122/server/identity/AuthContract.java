package edu.uit.se122.server.identity;

public interface AuthContract {
    record AdminRegisterReq(
            String name,
            String email,
            String password
    ) {}

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
            String name
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
}
