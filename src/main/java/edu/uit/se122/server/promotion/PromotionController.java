package edu.uit.se122.server.promotion;

import edu.uit.se122.server.promotion.internal.dto.PromotionReqDTO;
import edu.uit.se122.server.promotion.internal.dto.PromotionResDTO;
import edu.uit.se122.server.promotion.internal.service.PromotionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/promotions")
@RequiredArgsConstructor
public class PromotionController {

    private final PromotionService promotionService;

    @PostMapping
    public ResponseEntity<PromotionResDTO> create(@RequestBody PromotionReqDTO dto) {
        return ResponseEntity.ok(promotionService.create(dto));
    }

    @GetMapping
    public ResponseEntity<List<PromotionResDTO>> getAll() {
        return ResponseEntity.ok(promotionService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PromotionResDTO> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(promotionService.getById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PromotionResDTO> update(@PathVariable Integer id, @RequestBody PromotionReqDTO dto) {
        return ResponseEntity.ok(promotionService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        promotionService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
