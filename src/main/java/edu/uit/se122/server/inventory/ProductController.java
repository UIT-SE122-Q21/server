package edu.uit.se122.server.inventory;

import edu.uit.se122.server.inventory.internal.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/product")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @PostMapping
    public ResponseEntity<Object> create(@RequestBody ProductContract.Request dto) {
        productService.create(dto);
        return ResponseEntity.ok(Map.of("message", "Thêm mới thành công"));
    }

    @GetMapping
    public ResponseEntity<List<ProductContract.Response>> getAll() {
        return ResponseEntity.ok(productService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductContract.Response> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(productService.getById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> update(@PathVariable Integer id, @RequestBody ProductContract.Request dto) {
        productService.update(id, dto);
        return ResponseEntity.ok(Map.of("message", "Chỉnh sửa thành công"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@PathVariable Integer id) {
        productService.delete(id);
        return ResponseEntity.ok(Map.of("message", "Xóa thành công"));
    }
}
