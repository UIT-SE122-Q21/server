package edu.uit.se122.server.social;

import edu.uit.se122.server.social.internal.service.TogetherService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/member/together")
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

    @PostMapping("/{courtOrderId}")
    public ResponseEntity<Object> create(@PathVariable Integer courtOrderId, @RequestBody TogetherContract.Req dto) {
        togetherService.create(courtOrderId, dto);
        return ResponseEntity.ok(Map.of("message", "Thêm mới thành công"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> join(@PathVariable Integer id, @AuthenticationPrincipal Integer memberId) {
        togetherService.join(id, memberId);
        return ResponseEntity.ok(Map.of("message", "Tham gia cuộc hẹn thành công"));
    }

    @PutMapping("/{togetherId}/plan-manually")
    public ResponseEntity<Object> planManually(@PathVariable Integer togetherId) {
        togetherService.planManually(togetherId);
        return ResponseEntity.ok(Map.of("message", "Đã chốt hẹn"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> cancel(@PathVariable Integer id) {
        togetherService.cancel(id);
        return ResponseEntity.ok(Map.of("message", "Hủy cuộc hẹn thành công"));
    }
}
