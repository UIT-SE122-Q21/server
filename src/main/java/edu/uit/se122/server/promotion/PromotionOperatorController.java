package edu.uit.se122.server.promotion;

import edu.uit.se122.server.promotion.internal.service.PromotionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/operator/promotion")
@RequiredArgsConstructor
public class PromotionOperatorController {

    private final PromotionService promotionService;

    @PostMapping
    public ResponseEntity<Object> create(@RequestBody PromotionContract.CreateReq dto) {
        promotionService.create(dto);
        return ResponseEntity.ok(Map.of("message", "Thêm mới thành công"));
    }

    @GetMapping
    public ResponseEntity<List<PromotionContract.ResByOperator>> getAll() {
        return ResponseEntity.ok(promotionService.getAllByOperator());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PromotionContract.ResByOperator> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(promotionService.getByIdByOperator(id));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Object> hide(@PathVariable Integer id, @RequestBody PromotionContract.HideReq dto) {
        promotionService.hide(id, dto);
        return ResponseEntity.ok(Map.of("message", "Chỉnh sửa thành công"));
    }
}
