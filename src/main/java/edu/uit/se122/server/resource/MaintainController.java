package edu.uit.se122.server.resource;

import edu.uit.se122.server.resource.internal.service.MaintainService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/maintain")
@RequiredArgsConstructor
public class MaintainController {
    private final MaintainService maintainService;

    @GetMapping
    public ResponseEntity<List<MaintainContract.Res>> getAll() {
        return ResponseEntity.ok(maintainService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MaintainContract.Res> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(maintainService.getById(id));
    }

    @PostMapping
    public ResponseEntity<Object> create(@RequestBody MaintainContract.Req dto) {
        maintainService.create(dto);
        return ResponseEntity.ok(Map.of("message", "Thêm mới thành công"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> update(@PathVariable Integer id, @RequestBody MaintainContract.Req dto) {
        maintainService.update(id, dto);
        return ResponseEntity.ok(Map.of("message", "Chỉnh sửa thành công"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@PathVariable Integer id) {
        maintainService.delete(id);
        return ResponseEntity.ok(Map.of("message", "Xóa thành công"));
    }
}
