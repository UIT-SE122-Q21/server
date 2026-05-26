package edu.uit.se122.server.identity;

import edu.uit.se122.server.identity.internal.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/employee")
@RequiredArgsConstructor
public class EmployeeAdminController {
    private final AdminService adminService;

    @GetMapping()
    public ResponseEntity<List<EmployeeContract.Res>> getAll() {
        return ResponseEntity.ok(adminService.getAll());
    }

    @PostMapping
    public ResponseEntity<Object> create(@RequestBody EmployeeContract.CreateEmployeeReq dto) {
        adminService.createEmployee(dto);
        return ResponseEntity.ok(Map.of("message", "Tạo nhân viên thành công"));
    }

    @PostMapping("/{id}/change-password")
    public ResponseEntity<Object> changePassword(@PathVariable Integer id, @RequestBody EmployeeContract.ChangePasswordReq dto) {
        adminService.changePassword(id, dto);
        return ResponseEntity.ok(Map.of("message", "Thay đổi mật khẩu thành công"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> update(@PathVariable Integer id, @RequestBody EmployeeContract.UpdateReq dto) {
        adminService.update(id, dto);
        return ResponseEntity.ok(Map.of("message", "Chỉnh sửa thành công"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@PathVariable Integer id) {
        adminService.delete(id);
        return ResponseEntity.ok(Map.of("message", "Xóa thành công"));
    }
}
