package edu.uit.se122.server.resource;

import edu.uit.se122.server.resource.internal.service.FacilityCriterionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/facility-criterion")
@RequiredArgsConstructor
public class FacilityCriterionController {
    private final FacilityCriterionService facilityCriterionService;

    @GetMapping
    public ResponseEntity<List<FacilityCriterionContract.Response>> getAll() {
        return ResponseEntity.ok(facilityCriterionService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<FacilityCriterionContract.Response> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(facilityCriterionService.getById(id));
    }

    @PostMapping
    public ResponseEntity<FacilityCriterionContract.Response> create(@RequestBody FacilityCriterionContract.Request dto) {
        return ResponseEntity.ok(facilityCriterionService.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<FacilityCriterionContract.Response> update(@PathVariable Integer id, @RequestBody FacilityCriterionContract.Request dto) {
        return ResponseEntity.ok(facilityCriterionService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        facilityCriterionService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
