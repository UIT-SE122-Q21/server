package edu.uit.se122.server.resource;

import edu.uit.se122.server.resource.internal.service.FacilityCategoryService;
import edu.uit.se122.server.resource.internal.service.FacilityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/facility")
@RequiredArgsConstructor
public class FacilityController {
    private final FacilityService facilityService;

    @PostMapping
    public ResponseEntity<Object> create(@RequestBody FacilityContract.CreateRequest dto) {
        facilityService.create(dto);
        return ResponseEntity.ok(Map.of("message", "Thêm mới thành công"));
    }

    @GetMapping
    public ResponseEntity<List<FacilityContract.Response>> getAll() {
        return ResponseEntity.ok(facilityService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<FacilityContract.Response> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(facilityService.getById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> update(@PathVariable Integer id, @RequestBody FacilityContract.UpdateRequest dto) {
        facilityService.update(id, dto);
        return ResponseEntity.ok(Map.of("message", "Chỉnh sửa thành công"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@PathVariable Integer id) {
        facilityService.delete(id);
        return ResponseEntity.ok(Map.of("message", "Xóa thành công"));
    }
}
