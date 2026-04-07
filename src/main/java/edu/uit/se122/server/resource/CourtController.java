package edu.uit.se122.server.resource;

import edu.uit.se122.server.resource.internal.service.CourtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/court")
@RequiredArgsConstructor
public class CourtController {
    private final CourtService courtService;

    @GetMapping
    public ResponseEntity<List<CourtContract.Response>> getAll() {
        return ResponseEntity.ok(courtService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CourtContract.Response> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(courtService.getById(id));
    }

    @PostMapping
    public ResponseEntity<CourtContract.Response> create(@RequestBody CourtContract.Request dto) {
        return ResponseEntity.ok(courtService.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CourtContract.Response> update(@PathVariable Integer id, @RequestBody CourtContract.Request dto) {
        return ResponseEntity.ok(courtService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        courtService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
