package edu.uit.se122.server.social;

import edu.uit.se122.server.social.internal.service.TogetherService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/together")
@RequiredArgsConstructor
public class TogetherController {
    private final TogetherService togetherService;

    @GetMapping
    public ResponseEntity<List<TogetherContract.Res>> getAll() {
        return ResponseEntity.ok(togetherService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TogetherContract.Res> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(togetherService.getById(id));
    }

    @PostMapping
    public ResponseEntity<Object> create(@RequestBody TogetherContract.Req dto) {
        togetherService.create(dto);
        return ResponseEntity.ok(Map.of("message", "Thêm mới thành công"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> join(@PathVariable Integer id, @AuthenticationPrincipal Integer memberId) {
        togetherService.join(id, memberId);
        return ResponseEntity.ok(Map.of("message", "Tham gia cuộc hẹn thành công"));
    }

    @PutMapping("/{togetherId}/plan")
    public ResponseEntity<Object> plan(@PathVariable Integer togetherId) {
        togetherService.plan(togetherId);
        return ResponseEntity.ok(Map.of("message", "Đã chốt hẹn"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        togetherService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
