package edu.uit.se122.server.identity.internal.service;

import edu.uit.se122.server.common.enums.AdminRole;
import edu.uit.se122.server.common.security.JwtService;
import edu.uit.se122.server.identity.AuthContract;
import edu.uit.se122.server.identity.internal.entity.Administrator;
import edu.uit.se122.server.identity.internal.repository.AdministratorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AdministratorRepository administratorRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthContract.AdminBasic register(AuthContract.RegisterRequest dto) {
        Administrator admin = new Administrator();
        updateEntity(admin, dto);
        Administrator saved = administratorRepository.save(admin);

        return mapToRegister(saved);
    }

    public AuthContract.LoginResponse login(AuthContract.LoginRequest dto) {
        Administrator admin = administratorRepository.findById(dto.adminId())
                .orElseThrow(() -> new RuntimeException("Tài khoản không tồn tại!"));

        if (!passwordEncoder.matches(dto.password(), admin.getPassword())) {
            throw new RuntimeException("Mật khẩu không chính xác!");
        }
        AuthContract.AdminBasic adminBasic = new AuthContract.AdminBasic(admin.getAdminId(), admin.getName(), admin.getRole());
        String jwtToken = jwtService.generateToken(adminBasic);

        return new AuthContract.LoginResponse(jwtToken, admin.getAdminId(), admin.getName());
    }

    private void updateEntity(Administrator entity, AuthContract.RegisterRequest dto) {
        entity.setName(dto.name());
        entity.setPassword(passwordEncoder.encode(dto.password()));
        entity.setRole(dto.role() != null ? dto.role() : AdminRole.Manager);
    }

    private AuthContract.AdminBasic mapToRegister(Administrator entity) {
        return new AuthContract.AdminBasic(
                entity.getAdminId(),
                entity.getName(),
                entity.getRole()
        );
    }
}
