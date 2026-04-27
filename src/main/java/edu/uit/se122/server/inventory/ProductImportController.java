package edu.uit.se122.server.inventory;

import edu.uit.se122.server.inventory.internal.service.ProductImportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/product-import")
@RequiredArgsConstructor
public class ProductImportController {
    private final ProductImportService importService;

    @GetMapping
    public ResponseEntity<List<ProductImportContract.Res>> getAll() {
        return ResponseEntity.ok(importService.getAll());
    }

    @PostMapping
    public ResponseEntity<Object> create(@RequestBody ProductImportContract.CreateReq dto) {
        importService.create(dto);
        return ResponseEntity.ok(Map.of("message", "Thêm mới thành công"));
    }
}
