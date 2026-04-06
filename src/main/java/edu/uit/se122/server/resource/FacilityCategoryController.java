package edu.uit.se122.server.resource;

import edu.uit.se122.server.resource.internal.service.FacilityCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/facility-category")
@RequiredArgsConstructor
public class FacilityCategoryController {
    private final FacilityCategoryService facilityCategoryService;

    @PostMapping
    public ResponseEntity<FacilityCategoryContract.Response> create(@RequestBody FacilityCategoryContract.Request dto) {
        return ResponseEntity.ok(facilityCategoryService.create(dto));
    }

    @GetMapping
    public ResponseEntity<List<FacilityCategoryContract.Response>> getAll() {
        return ResponseEntity.ok(facilityCategoryService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<FacilityCategoryContract.Response> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(facilityCategoryService.getById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<FacilityCategoryContract.Response> update(@PathVariable Integer id, @RequestBody FacilityCategoryContract.Request dto) {
        return ResponseEntity.ok(facilityCategoryService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        facilityCategoryService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
