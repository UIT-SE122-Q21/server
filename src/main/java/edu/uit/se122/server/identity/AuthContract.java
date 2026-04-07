package edu.uit.se122.server.identity;

import edu.uit.se122.server.common.enums.AdminRole;

public interface AuthContract {
    record RegisterRequest(
            String name,
            String password,
            AdminRole role
    ) {}

    record LoginRequest(
            Integer adminId,
            String password
    ) {}

    record AdminBasic(
            Integer adminId,
            String name,
            AdminRole role
    ) {}

    record LoginResponse(
            String token,
            Integer adminId,
            String name
    ) {}
}
