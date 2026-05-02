package edu.uit.se122.server.identity.internal.service;

import edu.uit.se122.server.identity.AdminContract;
import edu.uit.se122.server.identity.AuthContract;
import edu.uit.se122.server.identity.internal.entity.Administrator;
import edu.uit.se122.server.identity.internal.repository.AdministratorRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class AdminService {
    private final AdministratorRepository administratorRepository;

    public List<AdminContract.Res> getAll() {
        return administratorRepository.findAll().stream()
                .filter(admin -> admin.getDeletedAt() == null)
                .map(this::mapToDTO).toList();
    }

    public void update(Integer id, AdminContract.UpdateReq dto) {
        Administrator administrator = administratorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Admin not found"));
        administrator.setName(dto.adminName());
        administrator.setEmail(dto.email());
        administrator.setPhoneNumber(dto.phoneNumber());
        administrator.setColor(dto.color());
    }

    public void delete(Integer id) {
        Administrator administrator = administratorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Admin not found"));
        administrator.setDeletedAt(LocalDateTime.now());
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
