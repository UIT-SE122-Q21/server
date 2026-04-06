package edu.uit.se122.server.resource;

import edu.uit.se122.server.resource.internal.service.FacilityCategoryService;
import edu.uit.se122.server.resource.internal.service.FacilityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/facility")
@RequiredArgsConstructor
public class FacilityController {
    private final FacilityService facilityService;

    @PostMapping
    public ResponseEntity<FacilityContract.Response> create(@RequestBody FacilityContract.CreateRequest dto) {
        return ResponseEntity.ok(facilityService.create(dto));
    }

    @GetMapping
    public ResponseEntity<List<FacilityContract.Response>> getAll() {
        return ResponseEntity.ok(facilityService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<FacilityContract.Response> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(facilityService.getById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<FacilityContract.Response> update(@PathVariable Integer id, @RequestBody FacilityContract.UpdateRequest dto) {
        return ResponseEntity.ok(facilityService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        facilityService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
