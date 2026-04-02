package edu.uit.se122.server.inventory;

import edu.uit.se122.server.inventory.internal.dto.ProductCategoryReqDTO;
import edu.uit.se122.server.inventory.internal.dto.ProductCategoryResDTO;
import edu.uit.se122.server.inventory.internal.service.ProductCategoryService;
import edu.uit.se122.server.promotion.internal.dto.PromotionReqDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/product-category")
@RequiredArgsConstructor
public class ProductCategoryController {

    private final ProductCategoryService productCategoryService;

    @PostMapping
    public ResponseEntity<ProductCategoryResDTO> create(@RequestBody ProductCategoryReqDTO dto) {
        return ResponseEntity.ok(productCategoryService.create(dto));
    }

    @GetMapping
    public ResponseEntity<List<ProductCategoryResDTO>> getAll() {
        return ResponseEntity.ok(productCategoryService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductCategoryResDTO> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(productCategoryService.getById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductCategoryResDTO> update(@PathVariable Integer id, @RequestBody ProductCategoryReqDTO dto) {
        return ResponseEntity.ok(productCategoryService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        productCategoryService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
