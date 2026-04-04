package edu.uit.se122.server.maintenance;

import edu.uit.se122.server.maintenance.internal.service.BrokenReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/broken-report")
@RequiredArgsConstructor
public class BrokenReportController {

    private final BrokenReportService brokenReportService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<BrokenReportContract.Response> create(@ModelAttribute BrokenReportContract.Request request) throws IOException {
        return ResponseEntity.ok(brokenReportService.create(request));
    }

    @GetMapping
    public ResponseEntity<List<BrokenReportContract.Response>> getAll() {
        return ResponseEntity.ok(brokenReportService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BrokenReportContract.Response> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(brokenReportService.getById(id));
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<BrokenReportContract.Response> update(
            @PathVariable Integer id,
            @ModelAttribute BrokenReportContract.Request request) throws IOException {
        return ResponseEntity.ok(brokenReportService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        brokenReportService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
