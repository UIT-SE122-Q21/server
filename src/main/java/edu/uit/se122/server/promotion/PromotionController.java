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
    public ResponseEntity<Object> create(@RequestBody PromotionContract.CreateReq dto) {
        promotionService.create(dto);
        return ResponseEntity.ok(Map.of("message", "Thêm mới thành công"));
    }

    @GetMapping
    public ResponseEntity<List<PromotionContract.Res>> getAll() {
        return ResponseEntity.ok(promotionService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PromotionContract.Res> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(promotionService.getById(id));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Object> hide(@PathVariable Integer id, @RequestBody PromotionContract.HideReq dto) {
        promotionService.hide(id, dto);
        return ResponseEntity.ok(Map.of("message", "Chỉnh sửa thành công"));
    }
}
