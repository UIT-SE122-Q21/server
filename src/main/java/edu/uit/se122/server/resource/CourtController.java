package edu.uit.se122.server.resource;

import edu.uit.se122.server.resource.internal.service.CourtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/court")
@RequiredArgsConstructor
public class CourtController {
    private final CourtService courtService;

    @GetMapping
    public ResponseEntity<List<CourtContract.Response>> getAll() {
        return ResponseEntity.ok(courtService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CourtContract.Response> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(courtService.getById(id));
    }

    @PostMapping
    public ResponseEntity<Object> create(@RequestBody CourtContract.Request dto) {
        courtService.create(dto);
        return ResponseEntity.ok(Map.of("message", "Thêm mới thành công"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> update(@PathVariable Integer id, @RequestBody CourtContract.Request dto) {
        courtService.update(id, dto);
        return ResponseEntity.ok(Map.of("message", "Chỉnh sửa thành công"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@PathVariable Integer id) {
        courtService.delete(id);
        return ResponseEntity.ok(Map.of("message", "Xóa thành công"));
    }

    @PutMapping("/price")
    public ResponseEntity<Object> updateGlobalCourtPrice(@AuthenticationPrincipal Integer adminId, @RequestBody CourtContract.UpdateCourtPriceReq dto) {
        courtService.updateGlobalCourtPrice(adminId, dto);
        return ResponseEntity.ok(Map.of("message", "Cập nhật giá sân thành công"));
    }
}
