package edu.uit.se122.server.inventory;

import edu.uit.se122.server.inventory.internal.service.ProductCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/product-category")
@RequiredArgsConstructor
public class ProductCategoryController {

    private final ProductCategoryService productCategoryService;

    @PostMapping
    public ResponseEntity<Object> create(@RequestBody ProductCategoryContract.Request dto) {
        productCategoryService.create(dto);
        return ResponseEntity.ok(Map.of("message", "Thêm mới thành công"));
    }

    @GetMapping
    public ResponseEntity<List<ProductCategoryContract.Response>> getAll() {
        return ResponseEntity.ok(productCategoryService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductCategoryContract.Response> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(productCategoryService.getById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> update(@PathVariable Integer id, @RequestBody ProductCategoryContract.Request dto) {
        productCategoryService.update(id, dto);
        return ResponseEntity.ok(Map.of("message", "Chỉnh sửa thành công"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@PathVariable Integer id) {
        productCategoryService.delete(id);
        return ResponseEntity.ok(Map.of("message", "Xóa thành công"));
    }
}
