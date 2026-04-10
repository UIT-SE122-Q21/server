package edu.uit.se122.server.social;

import edu.uit.se122.server.social.internal.service.TogetherService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/together")
@RequiredArgsConstructor
public class TogetherController {
    private final TogetherService togetherService;

    @GetMapping
    public ResponseEntity<List<TogetherContract.Response>> getAll() {
        return ResponseEntity.ok(togetherService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TogetherContract.Response> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(togetherService.getById(id));
    }

    @PostMapping
    public ResponseEntity<Object> create(@RequestBody TogetherContract.Request dto) {
        togetherService.create(dto);
        return ResponseEntity.ok(Map.of("message", "Thêm mới thành công"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> update(@PathVariable Integer id, @RequestBody TogetherContract.Request dto) {
        togetherService.update(id, dto);
        return ResponseEntity.ok(Map.of("message", "Chỉnh sửa thành công"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        togetherService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
