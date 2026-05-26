package edu.uit.se122.server.identity.internal.service;

import edu.uit.se122.server.common.enums.AdminRole;
import edu.uit.se122.server.identity.EmployeeContract;
import edu.uit.se122.server.identity.internal.entity.Administrator;
import edu.uit.se122.server.identity.internal.repository.AdministratorRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class AdminService {
    private final AdministratorRepository administratorRepository;
    private final PasswordEncoder passwordEncoder;

    public List<EmployeeContract.Res> getAll() {
        return administratorRepository.findAll().stream()
                .filter(admin -> admin.getDeletedAt() == null)
                .map(this::mapToDTO).toList();
    }
    
    public void createEmployee(EmployeeContract.CreateEmployeeReq dto) {
        Integer maxId = administratorRepository.findMaxAdminId();
        int newAdminId;
        if (maxId == null) {
            newAdminId = 236 * 1000 + 1;
        } else {
            newAdminId = maxId + 1;
        }
        
        Administrator administrator = new Administrator();
        administrator.setAdminId(newAdminId);
        administrator.setName(dto.name());
        administrator.setEmail(dto.email());
        administrator.setPhoneNumber(dto.phoneNumber());
        administrator.setPassword(passwordEncoder.encode(dto.password()));
        administrator.setColor(dto.color());
        administrator.setRole(AdminRole.Employee);
        administratorRepository.save(administrator);
    }

    public void changePassword(Integer id, EmployeeContract.ChangePasswordReq dto) {
        Administrator administrator = administratorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Admin not found"));
        if (!passwordEncoder.matches(dto.oldPassword(), administrator.getPassword())) {
            throw new RuntimeException("Old password is incorrect");
        }
        administrator.setPassword(passwordEncoder.encode(dto.newPassword()));
        administratorRepository.save(administrator);
    }

    public void update(Integer id, EmployeeContract.UpdateReq dto) {
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

    private EmployeeContract.Res mapToDTO(Administrator entity) {
        return new EmployeeContract.Res(
                entity.getAdminId(),
                entity.getName(),
                entity.getEmail(),
                entity.getPhoneNumber(),
                entity.getColor(),
                entity.getRole()
        );
    }
}
