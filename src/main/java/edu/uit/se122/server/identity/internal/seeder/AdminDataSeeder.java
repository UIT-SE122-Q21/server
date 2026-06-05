package edu.uit.se122.server.identity.internal.seeder;

import edu.uit.se122.server.common.enums.AdminRole;
import edu.uit.se122.server.identity.internal.entity.Administrator;
import edu.uit.se122.server.identity.internal.repository.AdministratorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdminDataSeeder implements CommandLineRunner {
    private final AdministratorRepository adminRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (!adminRepository.existsById(236000)) {
            Administrator admin = new Administrator();
            admin.setAdminId(236000);
            admin.setName("Admin");
            admin.setPassword(passwordEncoder.encode("system@123"));
            admin.setRole(AdminRole.Manager);

            adminRepository.save(admin);
        }
    }
}
