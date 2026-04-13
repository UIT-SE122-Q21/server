package edu.uit.se122.server.promotion;

import edu.uit.se122.server.promotion.internal.service.PromotionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/promotions")
@RequiredArgsConstructor
public class PromotionController {

    private final PromotionService promotionService;

    @PostMapping
    public ResponseEntity<Object> create(@RequestBody PromotionContract.Request dto) {
        promotionService.create(dto);
        return ResponseEntity.ok(Map.of("message", "Thêm mới thành công"));
    }

    @GetMapping
    public ResponseEntity<List<PromotionContract.Response>> getAll() {
        return ResponseEntity.ok(promotionService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PromotionContract.Response> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(promotionService.getById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> update(@PathVariable Integer id, @RequestBody PromotionContract.Request dto) {
        promotionService.update(id, dto);
        return ResponseEntity.ok(Map.of("message", "Chỉnh sửa thành công"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@PathVariable Integer id) {
        promotionService.delete(id);
        return ResponseEntity.ok(Map.of("message", "Xóa thành công"));
    }
}
