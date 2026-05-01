package edu.uit.se122.server.identity.internal.service;

import edu.uit.se122.server.identity.AdminContract;
import edu.uit.se122.server.identity.internal.entity.Administrator;
import edu.uit.se122.server.identity.internal.repository.AdministratorRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class AdminService {
    private final AdministratorRepository administratorRepository;

    public List<AdminContract.Res> getAll() {
        return administratorRepository.findAll().stream().map(this::mapToDTO).toList();
    }

    private AdminContract.Res mapToDTO(Administrator entity) {
        return new AdminContract.Res(
                entity.getAdminId(),
                entity.getName(),
                entity.getEmail(),
                entity.getPhoneNumber(),
                entity.getColor(),
                entity.getRole()
        );
    }
}
