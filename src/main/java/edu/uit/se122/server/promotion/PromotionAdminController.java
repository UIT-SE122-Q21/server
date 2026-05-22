package edu.uit.se122.server.promotion;

import edu.uit.se122.server.promotion.internal.service.PromotionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/promotion")
@RequiredArgsConstructor
public class PromotionAdminController {

    private final PromotionService promotionService;

    @GetMapping
    public ResponseEntity<List<PromotionContract.ResByAdmin>> getAll() {
        return ResponseEntity.ok(promotionService.getAllByAdmin());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PromotionContract.ResByAdmin> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(promotionService.getByIdByAdmin(id));
    }
}
