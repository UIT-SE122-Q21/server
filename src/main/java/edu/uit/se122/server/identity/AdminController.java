package edu.uit.se122.server.identity;

import edu.uit.se122.server.identity.internal.service.AdminService;
import edu.uit.se122.server.identity.internal.service.AuthService;
import edu.uit.se122.server.inventory.ProductCategoryContract;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/employee")
@RequiredArgsConstructor
public class AdminController {
    private final AdminService adminService;

    @GetMapping()
    public ResponseEntity<List<AdminContract.Res>> getAll() {
        return ResponseEntity.ok(adminService.getAll());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> update(@PathVariable Integer id, @RequestBody AdminContract.UpdateReq dto) {
        adminService.update(id, dto);
        return ResponseEntity.ok(Map.of("message", "Chỉnh sửa thành công"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@PathVariable Integer id) {
        adminService.delete(id);
        return ResponseEntity.ok(Map.of("message", "Xóa thành công"));
    }
}
