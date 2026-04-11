package edu.uit.se122.server.resource;

import edu.uit.se122.server.resource.internal.service.FacilityCriterionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/facility-criterion")
@RequiredArgsConstructor
public class FacilityCriterionController {
    private final FacilityCriterionService facilityCriterionService;

    @GetMapping
    public ResponseEntity<List<FacilityCriterionContract.Response>> getAll() {
        return ResponseEntity.ok(facilityCriterionService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<FacilityCriterionContract.Response> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(facilityCriterionService.getById(id));
    }

    @PostMapping
    public ResponseEntity<Object> create(@RequestBody FacilityCriterionContract.Request dto) {
        return ResponseEntity.ok(Map.of("message", "Thêm mới thành công"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> update(@PathVariable Integer id, @RequestBody FacilityCriterionContract.Request dto) {
        return ResponseEntity.ok(Map.of("message", "Chỉnh sửa thành công"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@PathVariable Integer id) {
        facilityCriterionService.delete(id);
        return ResponseEntity.ok(Map.of("message", "Xóa thành công"));
    }
}
