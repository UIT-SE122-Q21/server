package edu.uit.se122.server.resource;

import edu.uit.se122.server.resource.internal.service.FacilityCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/facility-category")
@RequiredArgsConstructor
public class FacilityCategoryController {
    private final FacilityCategoryService facilityCategoryService;

    @PostMapping
    public ResponseEntity<Object> create(@RequestBody FacilityCategoryContract.Request dto) {
        facilityCategoryService.create(dto);
        return ResponseEntity.ok(Map.of("message", "Thêm mới thành công"));
    }

    @GetMapping
    public ResponseEntity<List<FacilityCategoryContract.Response>> getAll() {
        return ResponseEntity.ok(facilityCategoryService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<FacilityCategoryContract.Response> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(facilityCategoryService.getById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> update(@PathVariable Integer id, @RequestBody FacilityCategoryContract.Request dto) {
        facilityCategoryService.update(id, dto);
        return ResponseEntity.ok(Map.of("message", "Chỉnh sửa thành công"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@PathVariable Integer id) {
        facilityCategoryService.delete(id);
        return ResponseEntity.ok(Map.of("message", "Xóa thành công"));
    }
}
