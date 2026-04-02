package edu.uit.se122.server.maintenance;

import edu.uit.se122.server.maintenance.internal.dto.BrokenReportReqDTO;
import edu.uit.se122.server.maintenance.internal.dto.BrokenReportResDTO;
import edu.uit.se122.server.maintenance.internal.service.BrokenReportService;
import edu.uit.se122.server.promotion.internal.dto.PromotionReqDTO;
import edu.uit.se122.server.promotion.internal.dto.PromotionResDTO;
import edu.uit.se122.server.promotion.internal.service.PromotionService;
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
    public ResponseEntity<BrokenReportResDTO> create(@ModelAttribute BrokenReportReqDTO request) throws IOException {
        return ResponseEntity.ok(brokenReportService.create(request));
    }

    @GetMapping
    public ResponseEntity<List<BrokenReportResDTO>> getAll() {
        return ResponseEntity.ok(brokenReportService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BrokenReportResDTO> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(brokenReportService.getById(id));
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<BrokenReportResDTO> update(
            @PathVariable Integer id,
            @ModelAttribute BrokenReportReqDTO request) throws IOException {
        return ResponseEntity.ok(brokenReportService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        brokenReportService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
