package edu.uit.se122.server.identity;

import edu.uit.se122.server.common.enums.AdminRole;

public interface AdminContract {
    record Res(
            Integer adminId,
            String adminName,
            String email,
            String phoneNumber,
            String color,
            AdminRole role
    ) {}

    record UpdateReq(
            String adminName,
            String email,
            String phoneNumber,
            String color
    ) {}
}
