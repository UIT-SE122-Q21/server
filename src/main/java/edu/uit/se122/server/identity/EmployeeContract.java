package edu.uit.se122.server.identity;

import edu.uit.se122.server.common.enums.AdminRole;

public interface EmployeeContract {
    record Res(
            Integer adminId,
            String adminName,
            String email,
            String phoneNumber,
            String color,
            AdminRole role
    ) {}

    record CreateEmployeeReq(
            String name,
            String email,
            String phoneNumber,
            String password,
            String color
    ) {}

    record ChangePasswordReq(
            String oldPassword,
            String newPassword
    ) {}

    record UpdateReq(
            String adminName,
            String email,
            String phoneNumber,
            String color
    ) {}
}
